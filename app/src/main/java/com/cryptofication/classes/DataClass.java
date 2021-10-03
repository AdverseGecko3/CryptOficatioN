package com.cryptofication.classes;

import com.cryptofication.objects.Crypto;

import java.util.ArrayList;
import java.util.List;

public class DataClass {
    public static DatabaseClass db;
    public List<Crypto> cryptoList = new ArrayList<>();
    public static int oldItem = -25, newItem;
    public static boolean firstRun = true;
}
