package com.kakaopay.internet.util;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FileUtilTest {

    @Test
    public void read(){

        String filePath = getClass().getClassLoader().getResource("2019.csv").getPath();

        List<String> list = FileUtil.readFile(filePath);

        list.forEach(System.out::println);

        assertNotNull(list);
        assertEquals(9, list.size());
    }
}
