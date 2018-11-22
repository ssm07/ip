package com.iprocessor.DTO;

import java.util.Date;

public class Admin extends User {

    private Long adminId;  // not null , unique , primary key
    private Long createdBy;
    private Date adminCreatedDate;// admin creation date.

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getAdminCreatedDate() {
        return adminCreatedDate;
    }

    public void setAdminCreatedDate(Date adminCreatedDate) {
        this.adminCreatedDate = adminCreatedDate;
    }
}
