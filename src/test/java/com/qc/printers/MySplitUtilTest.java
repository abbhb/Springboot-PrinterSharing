package com.qc.printers;

import com.qc.printers.utils.permissionstringsplit.MySplit;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MySplitUtilTest {
    @Test
    void firsttest() {
        String s = MySplit.splitString("1⌘1⌾23⌾1⌘1123", 1);
        System.out.println(s);
    }
}
