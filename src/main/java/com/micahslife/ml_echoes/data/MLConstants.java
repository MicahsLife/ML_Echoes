package com.micahslife.ml_echoes.data;

import com.google.gson.Gson;
import com.hypixel.hytale.logger.HytaleLogger;

import java.util.Random;

public class MLConstants {

    public static final Gson GSON = new Gson();
    public static final HytaleLogger LOGGER = HytaleLogger.get("Hybrid");
    public static final Random RANDOM = new Random();

}
