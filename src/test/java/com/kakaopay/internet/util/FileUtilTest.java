package com.kakaopay.internet.util;

import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.util.ResourceUtils.*;

public class FileUtilTest {

    @Test
    public void read() {

        List<String> list = FileUtil.readFile("classpath:2019.csv");

        list.forEach(System.out::println);

        assertNotNull(list);
        assertEquals(9, list.size());
    }
}
