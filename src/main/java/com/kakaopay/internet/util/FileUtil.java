package com.kakaopay.internet.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FileUtil {

    public static List<String> readFile(String filePath){

        List<String> list = null;

        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){

            list = br.lines().skip(1).collect(Collectors.toList());

        }catch (FileNotFoundException e){
            log.error(e.getMessage());
        }catch (IOException ex){
            log.error(ex.getMessage());
        }

        return list;
    }

}
