package com.kakaopay.internet.util;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FileUtilTest {

    @Test
    public void read() {

        List<String> list = FileUtil.readFile("2019.csv");

        list.forEach(System.out::println);

        assertNotNull(list);
        assertEquals(9, list.size());
    }
}
