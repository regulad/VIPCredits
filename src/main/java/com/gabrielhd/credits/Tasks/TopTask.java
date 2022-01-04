package com.gabrielhd.credits.Tasks;

import com.gabrielhd.common.MySQL;
import com.gabrielhd.credits.Main;

public class TopTask implements Runnable {
    @Override
    public void run() {
        Main.setTop(MySQL.getInstance().getTop());
    }
}
