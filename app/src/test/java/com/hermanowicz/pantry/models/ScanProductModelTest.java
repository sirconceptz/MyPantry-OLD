package com.hermanowicz.pantry.models;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ScanProductModelTest {

    private ScanProductModel model = new ScanProductModel();

    @Test
    public void decodeScanResult() {
        String happyScenario = "{\"product_id\":5,\"hash_code\":123456789}";
        List<Integer> decodedScan = model.decodeScanResult(happyScenario);

        Assert.assertEquals(5, (int) decodedScan.get(0));
        Assert.assertEquals(123456789, (int) decodedScan.get(1));
    }
}