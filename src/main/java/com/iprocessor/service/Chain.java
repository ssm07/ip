package com.iprocessor.service;

import com.iprocessor.DTO.ImageDTO;

public interface Chain {

     void  setNextChain(Chain nextChain);
     ImageDTO applyFilter(ImageDTO imageDTO);
}
