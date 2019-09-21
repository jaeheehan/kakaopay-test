package com.kakaopay.internet.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FileUtil {

    public static List<String> readFile(String filePath){

        List<String> list = null;

        ClassPathResource resource = new ClassPathResource(filePath);

        try(BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))){

            list = br.lines().collect(Collectors.toList());

        }catch (FileNotFoundException e){
            log.error(e.getMessage());
        }catch (IOException ex){
            log.error(ex.getMessage());
        }

        return list;
    }

}
