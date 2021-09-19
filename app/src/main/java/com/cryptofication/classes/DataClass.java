package com.cryptofication.classes;

import com.cryptofication.objects.Crypto;

import java.util.ArrayList;
import java.util.List;

public class DataClass {
    public List<Crypto> cryptoList = new ArrayList<>();
    public static int oldItem = -25, newItem;
    public int orderOption = 0;
    public int orderFilter = 0;
    public static boolean firstRun = true;
}
