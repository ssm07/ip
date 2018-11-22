package com.iprocessor.repository;

import com.iprocessor.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

@Repository
public class UserRepository {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int addUser(User user) {
        System.out.println(" in add user method ::User Repository \n\n"+user.toString());

        int update = jdbcTemplate.update(" INSERT INTO  user_profile  (username,password,first_name,last_name,email_id,premium_user,admin," +
                        "created_date,resource_usage,path " +
                        ") VALUES  (?,?,?,?,?,?,?,?,?,?) ",
                user.getUserName(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmailId(), user.getPremiumUser(),
                user.getAdmin() , new Date(), user.getResourceUsage(), user.getPath());

         System.out.println(" update "+update);
        return update;
    }



    public PremiumUser getPremiumUser(User user){
        String sql= " SELECT *  from premium_user_profile where username = ? ";

          List<PremiumUser> premiumUsers= jdbcTemplate.query(sql, new Object[]{user.getUserName()},new BeanPropertyRowMapper(PremiumUser.class));
          return  premiumUsers.get(0);
    }

    public PremiumUser addPremiumUSer(PremiumUser premiumUser) {


        String sql = "INSERT  INTO premium_user_profile (username,premium_user_created_date,premium_user_expiration_date,isactive) values (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, premiumUser.getUserName());
            ps.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            ps.setDate(3, new java.sql.Date(premiumUser.getPremiumUserExpirationDate().getTime()));
            ps.setInt(4,1);
            return ps;

        }, keyHolder);
        Number number = keyHolder.getKey();
        premiumUser.setPremiumUserId(number.longValue());
        return premiumUser;
    }

    public int addAdmin(Admin admin) {
        int update = jdbcTemplate.update("INSERT  INTO admin_user_profile (created_by,admin_created_date,username) VALUES (?,?,?)" +
                admin.getCreatedBy(), new Date(), admin.getUserName());
        return update;
    }



    public int  upgradeUser(User user) {

        Object[] param = {user.getUserName()};
        return jdbcTemplate.update(" UPDATE  user_profile  SET  premium_user=1  where username=? ", param);

    }

    public User login(User user) {

        User userObj = null;
        try {
            userObj = (User) jdbcTemplate.queryForObject(" SELECT * FROM user_profile  WHERE username=?  AND  password= ?",
                    new Object[]{user.getUserName(), user.getPassword()}, new BeanPropertyRowMapper(User.class));
        } catch (InvalidResultSetAccessException e) {
            // log the error
        } catch (DataAccessException e) {

        }
        return userObj;
    }


    public boolean updateResourceUsage(long resourceUsage, String userName) {
        Object[] params = {resourceUsage, userName};
        int updateRows = jdbcTemplate.update(" UPDATE  user_profile  SET resource_usage= ? WHERE  username=?", params);
        return updateRows > 0 ? true : false;
    }

    public List<User> getAllPremiumUserForWeekExpirationNotification() {

        StringBuilder sql= new StringBuilder("  select  u.first_name, u.last_name,u.email_id    from  user_profile u, ");
        sql.append(" premium_user_profile pu    where pu.username=u.username");
        sql.append("   and (pu.premium_user_expiration_date -interval 7 day)  <  CURDATE() ");
        System.out.println(" Executing SQL \n"+sql.toString());
        List<User> userList = jdbcTemplate.query(sql.toString(),
                new RowMapper<User>() {
                    @Nullable
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User user= new User();
                        user.setFirstName(rs.getString("first_name"));
                        user.setLastName(rs.getString("last_name"));
                        user.setEmailId(rs.getString("email_id"));
                        return  user;
                    }
                });

        return userList;
    }

    public  int  addPayment(Payment payment,long premiumUserId,String userName){

        KeyHolder keyHolder = new GeneratedKeyHolder();
        StringBuilder sql=  new StringBuilder(" INSERT INTO payment (premium_user_id,username,payment_date,card_number,cvv,card_expiration_date,card_holder_name,payment_amount)" );
        sql.append(" values (?,?,?,?,?,?,?,?)");
        int result=jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, (int)premiumUserId);
            ps.setString(2,userName);
            ps.setDate(3,new java.sql.Date(System.currentTimeMillis()));
            ps.setLong(4,payment.getCardNumber());
            ps.setInt(5,(int)payment.getCvv());
            ps.setString(6,payment.getExpirationDateAsString());
            ps.setString(7,payment.getCardHolderNumber());
            ps.setInt(8,payment.getPaymentAmount());
            return ps;

        }, keyHolder);
        Number number = keyHolder.getKey();
        payment.setPaymentId(number.longValue());
        return result;

    }


    public List<ReportDTO> getStandardUserForReport(Date date, boolean isStartDate){
        StringBuilder sql= new StringBuilder(getStandardUserForReportQuery());
        if(isStartDate){
            sql.append(" where  created_date > ?");
        }else {
            sql.append(" where  created_date < ? ");
        }
        List<ReportDTO> reportDTOList=jdbcTemplate.query(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setDate(1,new java.sql.Date(date.getTime()));
            }
        },new ReportDTOMapperForStandardUser());
        return  reportDTOList;

    }

    public List<ReportDTO> getStandardUserForReport(Date startDate,Date endDate ){

        StringBuilder sql= new StringBuilder(" SELECT * FROM user_profile");
        sql.append(" WHERE   created_date  between  ? and  ?  ");
        System.out.println(" Query "+sql);
        System.out.println(" Parameters start date"+ startDate +" end date "+endDate);
        List<ReportDTO> reportDTOList=jdbcTemplate.query(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setDate(1,new java.sql.Date(startDate.getTime()));
                ps.setDate(2,new java.sql.Date(endDate.getTime()));

            }
        },new ReportDTOMapperForStandardUser());
        return reportDTOList;

    }
    public List<ReportDTO> getStandardUserForReport(){

        StringBuilder sql= new StringBuilder(getStandardUserForReportQuery());
        List<ReportDTO> reportDTOList=jdbcTemplate.query(sql.toString(), new ReportDTOMapperForStandardUser());
        return  reportDTOList;
    }

    public String getStandardUserForReportQuery(){
        StringBuilder sql= new StringBuilder("SELECT first_name,last_name,created_date,resource_usage FROM user_profile ");
        return sql.toString();
    }

    public List<ReportDTO> getPremiumUserDataForReport(Date date, boolean isStartDate ){

        StringBuilder sql= new StringBuilder(getPremiumUserDataForReportQuery());
        if(isStartDate){
            sql.append(" and  pu.premium_user_created_date > ?");
        }else {
            sql.append(" and  pu.premium_user_created_date < ? ");
        }
        List<ReportDTO> reportDTOList=jdbcTemplate.query(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setDate(1,new java.sql.Date(date.getTime()));
            }
        },new ReportDTOMapperForPremiumUser());
        return  reportDTOList;
    }

    public List<ReportDTO> getPremiumUserDataForReport(){
        String sql=getPremiumUserDataForReportQuery();
        List<ReportDTO> reportDTOList= jdbcTemplate.query(sql, new ReportDTOMapperForPremiumUser());
       return  reportDTOList;
    }


    private String getPremiumUserDataForReportQuery(){

        StringBuilder sql= new StringBuilder(" SELECT  u.first_name ,u.last_name,pu.premium_user_created_date ,pu.premium_user_expiration_date,u.resource_usage ,");
        sql.append(" py.payment_amount  FROM  user_profile u, premium_user_profile pu , payment py ");
        sql.append( " WHERE  u.username=pu.username and py.username=pu.username  and pu.isactive=1");
        return  sql.toString();

    }

    public List<ReportDTO> getPremiumUserDataForReport(Date startDate ,Date endDate){

        StringBuilder sql= new StringBuilder(getPremiumUserDataForReportQuery());
        sql.append(" and pu.premium_user_created_date  between  ? and  ?  ");
        List<ReportDTO> reportDTOList=jdbcTemplate.query(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setDate(1,new java.sql.Date(startDate.getTime()));
                ps.setDate(2,new java.sql.Date(endDate.getTime()));

            }
        },new ReportDTOMapperForPremiumUser());
        return reportDTOList;
    }



     public  List<PremiumUser> getExpiredPremiumUsers(){
        StringBuilder  sql= new StringBuilder(" Select  username  from   premium_user_profile   where   premium_user_expiration_date <  CURDATE()");
        List<PremiumUser>  premiumUserList=jdbcTemplate.query(sql.toString(), new RowMapper<PremiumUser>() {
            @Nullable
            @Override
            public PremiumUser mapRow(ResultSet rs, int rowNum) throws SQLException {
                PremiumUser premiumUser= new PremiumUser();
                premiumUser.setUserName(rs.getString("username"));
                return  premiumUser;
            }
        });
        return  premiumUserList;

     }


     public void  deletePremiumUser(List<PremiumUser> premiumUserList){

         String sql=" Update premium_user_profile set isactive =0  where username =? ";
         jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
             @Override
             public void setValues(PreparedStatement ps, int i) throws SQLException {
                   ps.setString(1,premiumUserList.get(i).getUserName());
             }

             @Override
             public int getBatchSize() {
                 return premiumUserList.size();
             }
         });


         String sql1=" Update user_profile set premium_user=0 where username=?";
         jdbcTemplate.batchUpdate(sql1, new BatchPreparedStatementSetter() {
             @Override
             public void setValues(PreparedStatement ps, int i) throws SQLException {
                 ps.setString(1,premiumUserList.get(i).getUserName());
             }

             @Override
             public int getBatchSize() {
                 return premiumUserList.size();
             }
         });
     }

     /**
      *  This method delete user from the system.
      * */
     public void deleteUserFromSystem(User user){
         String premiumUserDeleteSql=" DELETE FROM premium_user_profile WHERE username =? ";
         String standardUserDeleteSql=" DELETE FROM user_profile WHERE username=?";
         String adminDeleteSql=" DELETE FROM   admin_user_profile WHERE username=?";
         String deletePayment=" DELETE FROM payment WHERE username=?";
         Object [] params={user.getUserName()};
         if(user.getAdmin() ==1){
             jdbcTemplate.update(adminDeleteSql,params);

         }else{
            jdbcTemplate.update(deletePayment,params);
            jdbcTemplate.update(premiumUserDeleteSql,params);
            jdbcTemplate.update(standardUserDeleteSql,params);
         }


     }

     /**
      * <p>
      *      This method updates  premium user expiration date.
      *
      * </p>
      *
      * */
   public void extendExpirationDate(PremiumUser premiumUser){

         String sql=" Update premium_user_profile set premium_user_expiration_date= ? where  user_name=?";
         jdbcTemplate.update(sql,premiumUser.getPremiumUserExpirationDate(),premiumUser.getUserName());
   }


   public List<User>  searchUsers(String searchString){
       StringBuilder sql= new StringBuilder(" Select * from user_profile where first_name like ? or last_name like ? ");
       List<User> users=jdbcTemplate.query(sql.toString(), new PreparedStatementSetter() {
           @Override
           public void setValues(PreparedStatement ps) throws SQLException {
             ps.setString(1,"%"+searchString+"%");
             ps.setString(2,"%"+searchString+"%");
           }
       },new SearchUser());
       return users;
   }



}

class SearchUser implements  RowMapper{
    @Nullable
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        User user= new User();
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmailId(rs.getString("email_id"));
        user.setPremiumUser(rs.getInt("premium_user"));
        user.setUserName(rs.getString("username"));
        return user;

    }
}

class ReportDTOMapperForPremiumUser implements  RowMapper{

    @Nullable
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        ReportDTO reportDTO= new ReportDTO();
        reportDTO.setFirstName(rs.getString("u.first_name"));
        reportDTO.setLastName(rs.getString("u.last_name"));
        reportDTO.setCreatedDate(rs.getDate("pu.premium_user_created_date"));
        reportDTO.setExpirationDate(rs.getDate("pu.premium_user_expiration_date"));
        reportDTO.setResourceUsage(rs.getLong("u.resource_usage"));
        reportDTO.setPaymentAmount(rs.getInt("py.payment_amount"));
        return  reportDTO;
    }
}

class ReportDTOMapperForStandardUser implements  RowMapper{

    @Nullable
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        ReportDTO reportDTO= new ReportDTO();
        reportDTO.setFirstName(rs.getString("first_name"));
        reportDTO.setLastName(rs.getString("last_name"));
        reportDTO.setCreatedDate(rs.getDate("created_date"));
        reportDTO.setResourceUsage(rs.getLong("resource_usage"));
        return  reportDTO;
    }
}





