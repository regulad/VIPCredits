package com.gabrielhd.credits.Tasks;

import com.gabrielhd.credits.Main;
import com.gabrielhd.common.MySQL;

public class TopTask implements Runnable {
    @Override
    public void run() {
        Main.setTop(MySQL.getInstance().getTop());
    }
}
