package com.example.game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Level {

    private JSONArray levels;
    private int screenWidth;
    private int screenHeight;
    private int ballRadius;
    private int holeRadius;

    public Level( int screenWidth, int screenHeight ) {
        levels = new JSONArray();
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        ballRadius = 37;
        holeRadius = 60;

        try {
            this.setLevel1();
            this.setLevel2();
            this.setLevel3();
            this.setLevel4();
            this.setLevel5();
            this.setLevel6();
            this.setLevel7();
            this.setLevel8();
            this.setLevel9();
            this.setLevel10();
            this.setLevel11();
            this.setLevel12();
            this.setLevel13();
            this.setLevel14();
            this.setLevel15();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getLevel(int i) {
        try {
            return levels.getJSONObject(i-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setLevel1() throws JSONException {
        JSONObject level = new JSONObject();

        JSONObject startPosition = new JSONObject();
        startPosition.put("x", 80);
        startPosition.put("y", 80);
        level.put("startPosition", startPosition);

        JSONObject successPosition = new JSONObject();
        successPosition.put("x", 2135);
        successPosition.put("y", 215);
        level.put("successPosition", successPosition);

        JSONArray holes = new JSONArray();
        JSONArray obstacles = new JSONArray();

        this.putHole(holes, 177, 324);
        this.putHole(holes, 597, 739);
        this.putHole(holes, 1335, 810);
        this.putHole(holes, 1335, 390);
        this.putHole(holes, 2153, 350);
        this.putHole(holes, 1952, 334);

        this.putObstacle(obstacles, 377, 17, 30, 700);
        this.putObstacle(obstacles, 1320, 450, 30, 300);
        this.putObstacle(obstacles, 1320, 880, 30, 200);

        level.put("holes", holes);
        level.put("obstacles", obstacles);

        levels.put(level);


    }

    private void setLevel2() throws JSONException {
        JSONObject level = new JSONObject();

        JSONObject startPosition = new JSONObject();
        startPosition.put("x", 200);
        startPosition.put("y", 971);
        level.put("startPosition", startPosition);

        JSONObject successPosition = new JSONObject();
        successPosition.put("x", 1667);
        successPosition.put("y", 419);
        level.put("successPosition", successPosition);

        JSONArray holes = new JSONArray();
        JSONArray obstacles = new JSONArray();

        for (double a = 0; a <= Math.PI/2; a += Math.PI/10){
            this.putHole(holes, (int) (1667 + 300 * Math.cos(a)), (int) (300 * Math.sin(a)));
        }
        this.putHole(holes, 60, 764);
        this.putHole(holes, 263, 300);
        this.putHole(holes, 1060, 700);
        this.putHole(holes, 790, 310);
        this.putHole(holes, 790, 1020);
        this.putHole(holes, 1210, 800);
        this.putHole(holes, 1060, 320);
        this.putHole(holes, 1490, 420);
        this.putHole(holes, 1860, 670);
        this.putHole(holes, 2240, 1020);


        this.putObstacle(obstacles, 323, 200, 30, 880);
        this.putObstacle(obstacles, 700, 0, 30, 880);
        this.putObstacle(obstacles, 1120, 200, 30, 880);
        this.putObstacle(obstacles, 1560, 220, 30, 430);
        this.putObstacle(obstacles, 1590, 480, 600, 30);

        level.put("holes", holes);
        level.put("obstacles", obstacles);

        levels.put(level);


    }

    private void setLevel3() throws JSONException {
        JSONObject level = new JSONObject();

        JSONObject startPosition = new JSONObject();
        startPosition.put("x", 80);
        startPosition.put("y", 80);
        level.put("startPosition", startPosition);

        JSONObject successPosition = new JSONObject();
        successPosition.put("x", 2135);
        successPosition.put("y", 1020);
        level.put("successPosition", successPosition);

        JSONArray holes = new JSONArray();
        JSONArray obstacles = new JSONArray();

        this.putHole(holes, 520, 63);
        this.putHole(holes, 2186, 63);
        this.putHole(holes, 1523, 185);
        this.putHole(holes, 80, 342);
        this.putHole(holes, 1523, 342);
        this.putHole(holes, 530, 420);
        this.putHole(holes, 1799, 420);
        this.putHole(holes, 2186, 342);
        this.putHole(holes, 530, 577);
        this.putHole(holes, 1799, 697);
        this.putHole(holes, 2021, 797);
        this.putHole(holes, 80, 930);

        this.putObstacle(obstacles, 0, 245, 1942, 37);
        this.putObstacle(obstacles, 398, 480, 1942, 37);
        this.putObstacle(obstacles, 0, 777, 1942, 37);
        this.putObstacle(obstacles, 398, 912, 1942, 37);

        level.put("holes", holes);
        level.put("obstacles", obstacles);

        levels.put(level);


    }

    private void setLevel4() throws JSONException {
        JSONObject level = new JSONObject();

        int y1 = 160;
        int y2 = 400;
        int y3 = 730;


        JSONObject startPosition = new JSONObject();
        startPosition.put("x", screenWidth/2 - ballRadius);
        startPosition.put("y", 80);
        level.put("startPosition", startPosition);

        JSONObject successPosition = new JSONObject();
        successPosition.put("x", screenWidth - 3*holeRadius);
        successPosition.put("y", screenHeight - holeRadius - 30);
        level.put("successPosition", successPosition);

        JSONArray holes = new JSONArray();
        JSONArray obstacles = new JSONArray();

        this.putHole(holes, 650, y1+37+holeRadius);
        this.putHole(holes, screenWidth-650, y1+37+holeRadius);
        this.putHole(holes, screenWidth/2, y2+20);
        this.putHole(holes, 630, y2+37+holeRadius);
        this.putHole(holes, screenWidth-630, y2+37+holeRadius);
        this.putHole(holes, 834+holeRadius, y2+20);
        this.putHole(holes, screenWidth-834-holeRadius, y2+20);
        this.putHole(holes, screenWidth/2, 630);
        this.putHole(holes, screenWidth/2 - 150, 630);
        this.putHole(holes, screenWidth/2 + 150, 630);
        this.putHole(holes, screenWidth/2 - 180, 760);
        this.putHole(holes, screenWidth/2 + 180, 760);
        this.putHole(holes, screenWidth/2, y3+20);

        this.putObstacle(obstacles, (screenWidth-1700)/2, y1, 1700, 37);
        this.putObstacle(obstacles, 0, y2, 834, 37);
        this.putObstacle(obstacles, screenWidth-834, y2, 834, 37);
        this.putObstacle(obstacles, 0, y3, 900, 37);
        this.putObstacle(obstacles, screenWidth-900, y3, 900, 37);

        level.put("holes", holes);
        level.put("obstacles", obstacles);

        levels.put(level);


    }

    private void setLevel5() throws JSONException {
        JSONObject level = new JSONObject();

        JSONObject startPosition = new JSONObject();
        startPosition.put("x", 1436);
        startPosition.put("y", 630);
        level.put("startPosition", startPosition);

        JSONObject successPosition = new JSONObject();
        successPosition.put("x", 760);
        successPosition.put("y", 507);
        level.put("successPosition", successPosition);

        JSONArray holes = new JSONArray();
        JSONArray obstacles = new JSONArray();

        this.putHole(holes, 1120, 530);
        this.putHole(holes, 900, 520);
        this.putHole(holes, 620, 650);
        this.putHole(holes, 540, 265);
        for (int i = 0; i < 3; i++) {
            this.putHole(holes, 295, 470 + 113 * i);
        }
        for (int i = 0; i < 3; i++) {
            this.putHole(holes, 65, 1020 - 113 * i);
        }
        this.putHole(holes, 875, 60);
        this.putHole(holes, 875, 280);
        this.putHole(holes, 1270, 825);
        for (int i = 0; i < 3; i++) {
            this.putHole(holes, 1621 + 100*i, 390 + 70*i);
        }
        for (int i = 1; i < 3; i++) {
            this.putHole(holes, 1621 + 100*2-100*i, 390 + 70*2 + 70*i);
        }
        for (int i = 0; i < 4; i++) {
            this.putHole(holes, 450+110*i, 921);
        }

        this.putObstacle(obstacles, 1436-ballRadius-60, 630-ballRadius-30, 37, 630+ballRadius+45+-(630-ballRadius-30));
        this.putObstacle(obstacles, 1436-ballRadius-23, 630+ballRadius+10, 2*ballRadius+87, 37);
        this.putObstacle(obstacles, 1436+ballRadius+30, 410, 37, 270);
        this.putObstacle(obstacles, 640, 410, 1436+ballRadius+30-640, 37);
        this.putObstacle(obstacles, 603, 200, 37, 390);
        this.putObstacle(obstacles, 1100, 610, 37, 170);
        this.putObstacle(obstacles, 880, 566, 37, 300);
        this.putObstacle(obstacles, 355, 830, 556, 37);
        this.putObstacle(obstacles, 355, 200, 37, 630);
        this.putObstacle(obstacles, 0, 430, 170, 37);
        this.putObstacle(obstacles, 1000, 0, 450, 280);
        this.putObstacle(obstacles, 1600, 0, 450, 280);
        this.putObstacle(obstacles, 1040, 880, 170, 37);
        this.putObstacle(obstacles, 1320, 880, screenWidth-1320, 37);



        level.put("holes", holes);
        level.put("obstacles", obstacles);

        levels.put(level);


    }

    private void setLevel6() throws JSONException {
        JSONObject level = new JSONObject();

        JSONObject startPosition = new JSONObject();
        startPosition.put("x", 185);
        startPosition.put("y", 525);
        level.put("startPosition", startPosition);

        JSONObject successPosition = new JSONObject();
        successPosition.put("x", 394);
        successPosition.put("y", 1000);
        level.put("successPosition", successPosition);

        JSONArray holes = new JSONArray();
        JSONArray obstacles = new JSONArray();

        this.putHole(holes, 777, 237);
        this.putHole(holes, 777, 390);
        this.putHole(holes, 1405, 353);
        this.putHole(holes, 1550, 237);
        this.putHole(holes, 2280, 390);
        this.putHole(holes, 1689, 503);
        this.putHole(holes, 2280, 503);
        this.putHole(holes, 1475, 817);
        this.putHole(holes, 1625, 817);
        this.putHole(holes, 1335, 877);
        this.putHole(holes, 2280, 1020);
        this.putHole(holes, 930, 1035);
        this.putHole(holes, 145, 872);

        this.putObstacle(obstacles, 148, 237, 400, 250);
        this.putObstacle(obstacles, 148, 562, 400, 250);
        this.putObstacle(obstacles, 603, 602, 100, 170);
        this.putObstacle(obstacles, 728, 450, 250, 330);
        for (int i = 0; i < 6; i++) {
            this.putObstacle(obstacles, 725 + 150 * i, 150, 140, 30);
        }
        for (int i = 0; i < 6; i++) {
            this.putObstacle(obstacles, 1349 + 140 * i, 413, 130, 30);
        }
        for (int i = 0; i < 5; i++) {
            this.putObstacle(obstacles, 985 + 140 * i, 727, 130, 30);
        }
        this.putObstacle(obstacles, 1535, 757, 30, 200);
        this.putObstacle(obstacles, 1275, 937, 140, 30);
        this.putObstacle(obstacles, 1425, 937, 140, 30);
        for (int i = 0; i < 4; i++) {
            this.putObstacle(obstacles, 400 + 45 * i, 862 + 57 * i, 39, 30);
        }
        for (int i = 0; i < 3; i++) {
            this.putObstacle(obstacles, 742 + 45 * i, 902 - 57 * i, 39, 30);
        }
        this.putObstacle(obstacles, 1069, 912, 39, 30);
        this.putObstacle(obstacles, 1030, 972, 39, 30);
        this.putObstacle(obstacles, 1069, 1032, 39, 30);


        level.put("holes", holes);
        level.put("obstacles", obstacles);

        levels.put(level);


    }

    private void setLevel7() throws JSONException {
        JSONObject level = new JSONObject();

        JSONObject startPosition = new JSONObject();
        startPosition.put("x", 77);
        startPosition.put("y", 525);
        level.put("startPosition", startPosition);

        JSONObject successPosition = new JSONObject();
        successPosition.put("x", 2100);
        successPosition.put("y", 525);
        level.put("successPosition", successPosition);

        JSONArray holes = new JSONArray();
        JSONArray obstacles = new JSONArray();

        for (int i = 0; i < 8; i++) {
            this.putHole(holes, 214 + i * 250, 525);
        }
        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0){
                this.putHole(holes, 340 + i * 250, 325);
            }
            else {
                this.putHole(holes, 340 + i * 250, 395);
            }
        }
        for (int i = 0; i < 3; i++) {
            this.putHole(holes, 590 + i * 500, 245);

        }

        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0){
                this.putHole(holes, 340 + i * 250, 725);
            }
            else {
                this.putHole(holes, 340 + i * 250, 655);
            }
        }
        for (int i = 0; i < 3; i++) {
            this.putHole(holes, 590 + i * 500, 805);

        }

        for (int i = 0; i < 9; i++) {
            this.putHole(holes, 214 + i * 250, 925);
        }
        for (int i = 0; i < 9; i++) {
            this.putHole(holes, 214 + i * 250, 125);
        }




        level.put("holes", holes);
        level.put("obstacles", obstacles);

        levels.put(level);

    }

    private void setLevel8() throws JSONException {
        JSONObject level = new JSONObject();

        JSONObject startPosition = new JSONObject();
        startPosition.put("x", 1686);
        startPosition.put("y", 70);
        level.put("startPosition", startPosition);

        JSONObject successPosition = new JSONObject();
        successPosition.put("x", 2260);
        successPosition.put("y", 590);
        level.put("successPosition", successPosition);

        JSONArray holes = new JSONArray();
        JSONArray obstacles = new JSONArray();

        this.putHole(holes, 1700, 200);
        this.putHole(holes, 1750, 350);
        this.putHole(holes, 2240, 135);
        this.putHole(holes, 1365, 437);
        this.putHole(holes, 1165, 437);
        for (double a = 0; a <= Math.PI; a += Math.PI/7){
            this.putHole(holes, (int) (1260 + 251 * Math.cos(a)), (int) (300 - 251 * Math.sin(a)));
        }
        this.putHole(holes, 695, 377);
        for (int i = 0; i < 3; i++) {
            this.putHole(holes, 502, 200 + 120 * i);
        }
        this.putHole(holes, 622, 60);
        this.putHole(holes, 342, 165);
        this.putHole(holes, 80, 80);
        this.putHole(holes, 80, 400);
        this.putHole(holes, 372, 550);
        this.putHole(holes, 512, 660);
        this.putHole(holes, 652, 660);
        for (double a = Math.PI; a <= 3 * Math.PI / 2; a += Math.PI/8){
            this.putHole(holes, (int) (372 + 256 + 256 * Math.cos(a)), (int) (660 - 256 * Math.sin(a)));
        }
        this.putHole(holes, 80, 800);
        this.putHole(holes, 200, 1000);
        this.putHole(holes, 880, 660);
        this.putHole(holes, 1100, 739);
        this.putHole(holes, 1570, 739);
        this.putHole(holes, 1335, 867);
        this.putHole(holes, 1920, 957);
        this.putHole(holes, 2260, 957);

        this.putObstacle(obstacles, 1606, 0, 30, 245);
        this.putObstacle(obstacles, 685, 0, 30, 245);
        this.putObstacle(obstacles, 1245, 245, 30, 265);
        this.putObstacle(obstacles, 402, 245, 30, 265);
        for (int i = 0; i < 10; i++) {
            this.putObstacle(obstacles, 402 + 196 * i, 500, 165, 30);
        }
        for (int i = 0; i < 7; i++) {
            this.putObstacle(obstacles, 990 + 196 * i, 650, 165, 30);
        }
        for (int i = 0; i < 6; i++) {
            this.putObstacle(obstacles, 790 + 180 * i, 927, 165, 30);
        }

        level.put("holes", holes);
        level.put("obstacles", obstacles);

        levels.put(level);


    }

    private void setLevel9() throws JSONException {
        JSONObject level = new JSONObject();

        JSONObject startPosition = new JSONObject();
        startPosition.put("x", 70);
        startPosition.put("y", 70);
        level.put("startPosition", startPosition);

        JSONObject successPosition = new JSONObject();
        successPosition.put("x", 2200);
        successPosition.put("y", 70);
        level.put("successPosition", successPosition);

        JSONArray holes = new JSONArray();
        JSONArray obstacles = new JSONArray();

        this.putHole(holes, 347, 240);
        this.putHole(holes, 80, 500);
        this.putHole(holes, 342, 827);
        this.putHole(holes, 342, 970);
        this.putHole(holes, 868, 827);
        this.putHole(holes, 868, 970);
        this.putHole(holes, 1050, 415);
        this.putHole(holes, 858, 251);
        this.putHole(holes, 1100, 125);
        this.putHole(holes, 1280, 80);
        this.putHole(holes, 1630, 80);
        this.putHole(holes, 1660, 637);
        this.putHole(holes, 1580, 265);
        this.putHole(holes, 1550, 722);
        this.putHole(holes, 1654, 970);
        this.putHole(holes, 2094, 970);
        this.putHole(holes, 2280, 1000);

        this.putObstacle(obstacles, 428, 30, 350, 437);
        this.putObstacle(obstacles, 428, 527, 350, 437);
        this.putObstacle(obstacles, 1150, 205, 350, 420);
        this.putObstacle(obstacles, 1150, 642, 350, 420);
        this.putObstacle(obstacles, 1714, 30, 350, 437);
        this.putObstacle(obstacles, 1714, 537, 350, 437);

        level.put("holes", holes);
        level.put("obstacles", obstacles);

        levels.put(level);


    }

    private void setLevel10() throws JSONException {
        JSONObject level = new JSONObject();

        JSONObject startPosition = new JSONObject();
        startPosition.put("x", 90);
        startPosition.put("y", 1000);
        level.put("startPosition", startPosition);

        JSONObject successPosition = new JSONObject();
        successPosition.put("x", 1500);
        successPosition.put("y", 540);
        level.put("successPosition", successPosition);

        JSONArray holes = new JSONArray();
        JSONArray obstacles = new JSONArray();

        double radius = 550;
        for (double a = -2 * Math.PI; a <= 3.5 * Math.PI; a += Math.PI/8){
            radius -= 10;
            this.putHole(holes, (int) (1470 + radius * Math.cos(a)), (int) (570 - radius * Math.sin(a)));
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 11; j++) {
                if (i % 2 == 0) {
                    this.putObstacle(obstacles, 150 + 150 * i, 120 + 90 * j, 20, 20);
                }
                else {
                    this.putObstacle(obstacles, 150 + 150 * i, 9 + 90 * j, 20, 20);
                }
            }
        }

        level.put("holes", holes);
        level.put("obstacles", obstacles);

        levels.put(level);


    }

    private void setLevel11() throws JSONException {
        JSONObject level = new JSONObject();

        JSONObject startPosition = new JSONObject();
        startPosition.put("x", 185);
        startPosition.put("y", 525);
        level.put("startPosition", startPosition);

        JSONObject successPosition = new JSONObject();
        successPosition.put("x", 2260);
        successPosition.put("y", 525);
        level.put("successPosition", successPosition);

        JSONArray holes = new JSONArray();
        JSONArray obstacles = new JSONArray();

        this.putHole(holes, 105, 725);
        this.putHole(holes, 105, 325);
        for (int i = 0; i < 5; i++) {
            this.putHole(holes, 475 + 400 * i, 80);
            this.putHole(holes, 475 + 400 * i, 1000);
        }

        for (int i = 0; i < 6; i++) {
            this.putHole(holes, 675 + 200 * i, 165 + 75 * i);
        }

        for (int i = 0; i < 5; i++) {
            this.putHole(holes, 675 + 200 * i, 915 - 75 * i);
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 7; j++) {
                if (i % 2 == 0) {
                    this.putObstacle(obstacles, 260 + 200 * i, 80 + 150 * j, 30, 20);
                }
                else {
                    if (j != 6){
                        this.putObstacle(obstacles, 260 + 200 * i, 150 + 150 * j, 30, 20);
                    }
                }
            }
        }

        level.put("holes", holes);
        level.put("obstacles", obstacles);

        levels.put(level);


    }

    private void setLevel12() throws JSONException {
        JSONObject level = new JSONObject();

        JSONObject startPosition = new JSONObject();
        startPosition.put("x", 100);
        startPosition.put("y", 837);
        level.put("startPosition", startPosition);

        JSONObject successPosition = new JSONObject();
        successPosition.put("x", 1300);
        successPosition.put("y", 530);
        level.put("successPosition", successPosition);

        JSONArray holes = new JSONArray();
        JSONArray obstacles = new JSONArray();

        for (int i = 0; i < 15; i++) {
            this.putHole(holes, 95 + 150 * i, 1000);
            this.putHole(holes, 120 + 150 * i, 80);
        }

        for (int i = 0; i < 9; i++) {
            this.putHole(holes, 240 + 149 * i, 300);
            this.putHole(holes, 95 + 149 * i, 700);
        }

        for (int i = 0; i < 5; i++) {
            this.putHole(holes, 890 + 149 * i, 410);
        }

        for (int i = 0; i < 2; i++) {
            this.putHole(holes, 990 + 170 * i, 530);
        }

        for (int i = 0; i < 4; i++) {
            this.putHole(holes, 1435 + 150 * i, 530);
        }

        for (int i = 0; i < 2; i++) {
            this.putHole(holes, 1435, 650 + 120 * i);
        }

        this.putHole(holes, 495, 810);
        this.putHole(holes, 493, 590);

        for (int i = 0; i < 3; i++) {
            this.putHole(holes, 485 - 80 * i, 439 + 80 * i);
        }

        for (int i = 0; i < 2; i++) {
            this.putHole(holes, 565 + 80 * i, 519 + 80 * i);
        }

        for (int i = 0; i < 3; i++) {
            this.putHole(holes, 1895, 760 + 120 * i);
        }

        for (int i = 0; i < 3; i++) {
            this.putHole(holes, 2230 - 120 * i, 398);
        }

        level.put("holes", holes);
        level.put("obstacles", obstacles);

        levels.put(level);

    }

    private void setLevel13() throws JSONException {
        JSONObject level = new JSONObject();

        JSONObject startPosition = new JSONObject();
        startPosition.put("x", 2100);
        startPosition.put("y", 210);
        level.put("startPosition", startPosition);

        JSONObject successPosition = new JSONObject();
        successPosition.put("x", 2200);
        successPosition.put("y", 1000);
        level.put("successPosition", successPosition);

        JSONArray holes = new JSONArray();
        JSONArray obstacles = new JSONArray();

        this.putHole(holes, 1900, 210);
        this.putHole(holes, 1800, 110);
        this.putHole(holes, 1800, 310);
        this.putHole(holes, 80, 80);
        this.putHole(holes, 80, 600);
        this.putHole(holes, 210, 820);
        this.putHole(holes, 410, 828);

        for (double a = Math.PI/5; a <=  4 * Math.PI/5; a += Math.PI/20){
            this.putHole(holes, (int) (745 + 500 * Math.cos(a)), (int) (735 - 500 * Math.sin(a)));
        }

        this.putHole(holes, 745, 470);
        this.putHole(holes, 645, 670);
        this.putHole(holes, 840, 1000);
        this.putHole(holes, 1050, 900);
        this.putHole(holes, 2260, 490);
        this.putHole(holes, 1450, 415);
        for (int i = 0; i < 2; i++) {
            this.putHole(holes, 1630 + 459 * i, 665);
        }
        for (int i = 0; i < 3; i++) {
            this.putHole(holes, 1500 + 230 * i, 820);
        }

        for (int i = 0; i < 4; i++){
            this.putObstacle(obstacles, 2140 - 230 * i, 400, 200, 30);
            this.putObstacle(obstacles, 1200 + 230 * i, 570, 200, 30);
        }

        this.putObstacle(obstacles, 1160, 420, 30, 230);

        for (int i = 0; i < 6; i++) {
            this.putObstacle(obstacles, 1150 + 200 * i, 880, 185, 30);
        }

        for (int i = 0; i < 7; i++) {
            this.putObstacle(obstacles, 300 + 200 * i, 145, 180, 30);
        }

        this.putObstacle(obstacles, 300, 420, 30, 490);

        this.putObstacle(obstacles, 730, 535, 30, 545);

        level.put("holes", holes);
        level.put("obstacles", obstacles);

        levels.put(level);

    }

    private void setLevel14() throws JSONException {
        JSONObject level = new JSONObject();

        JSONObject startPosition = new JSONObject();
        startPosition.put("x", 2230);
        startPosition.put("y", 800);
        level.put("startPosition", startPosition);

        JSONObject successPosition = new JSONObject();
        successPosition.put("x", 880);
        successPosition.put("y", 410);
        level.put("successPosition", successPosition);

        JSONArray holes = new JSONArray();
        JSONArray obstacles = new JSONArray();

        for (int i = 0; i < 4; i++ ) {
            this.putHole(holes, 980 + 130 * i, 180);
        }
        this.putHole(holes, 880, 530);
        this.putHole(holes, 720, 350);
        this.putHole(holes, 700, 540);
        this.putHole(holes, 500, 420);
        for (int i = 0; i < 3; i++) {
            this.putHole(holes, 2200, 640 - 230 * i);
        }
        for (int i = 0; i < 2; i++) {
            this.putHole(holes, 1750, 320 + 150 * i);
        }
        for (int i = 0; i < 2; i++) {
            this.putHole(holes, 1180 + 80 * i, 570 + 80 * i);
        }
        this.putHole(holes, 1570, 800);
        this.putHole(holes, 80, 820);
        this.putHole(holes, 260, 828);
        for (int i = 0; i < 2; i++) {
            this.putHole(holes, 430 - 70 * i, 60 + 70 * i);
        }

        this.putObstacle(obstacles, 1900, 850, 437, 230);
        for (int i = 0; i < 4; i ++){
            this.putObstacle(obstacles, 1480 + 230 * i, 700, 200, 30);
        }
        this.putObstacle(obstacles, 1475, 725, 30, 140);
        for (int i = 0; i < 4; i++) {
            this.putObstacle(obstacles, 610 + 230 * i, 835, 200, 30);
            this.putObstacle(obstacles, 630 + 230 * i, 100, 200, 30);
        }
        this.putObstacle(obstacles, 610, 650, 30, 185);
        for (int i = 0; i < 2; i++) {
            this.putObstacle(obstacles, 150 + 230 * i, 650, 200, 30);
            this.putObstacle(obstacles, 150 + 230 * i, 230, 200, 30);
        }
        this.putObstacle(obstacles, 150, 230, 30, 420);
        this.putObstacle(obstacles, 610, 100, 30, 130);
        this.putObstacle(obstacles, 1520, 100, 30, 130);
        this.putObstacle(obstacles, 1520, 230, 150, 30);
        this.putObstacle(obstacles, 1670, 230, 30, 270);
        for (int i = 0; i < 3; i++) {
            this.putObstacle(obstacles, 1070 + 200 * i, 470, 180, 30);
        }
        this.putObstacle(obstacles, 1070, 470, 30, 200);
        this.putObstacle(obstacles, 800, 670, 300, 30);
        this.putObstacle(obstacles, 770, 300, 30,  370);
        this.putObstacle(obstacles, 790, 300, 200, 30);
        this.putObstacle(obstacles, 1000, 300, 200, 30);


        level.put("holes", holes);
        level.put("obstacles", obstacles);

        levels.put(level);

    }

    private void setLevel15() throws JSONException {
        JSONObject level = new JSONObject();

        JSONObject startPosition = new JSONObject();
        startPosition.put("x", 600);
        startPosition.put("y", 480);
        level.put("startPosition", startPosition);

        JSONObject successPosition = new JSONObject();
        successPosition.put("x", 1740);
        successPosition.put("y", 480);
        level.put("successPosition", successPosition);

        JSONArray holes = new JSONArray();
        JSONArray obstacles = new JSONArray();

        this.putHole(holes, 241, 70);
        this.putHole(holes, 2340 - 241, 70);

        this.putHole(holes, 441, 70);
        this.putHole(holes, 2340 - 441, 70);

        this.putHole(holes, 610, 150);
        this.putHole(holes, 2340 - 610, 150);

        this.putHole(holes, 1070, 200);
        this.putHole(holes, 2340 - 1070, 200);

        this.putHole(holes, 810, 580);
        this.putHole(holes, 2340 - 810, 580);

        this.putHole(holes, 570, 680);
        this.putHole(holes, 2340 - 570, 680);

        this.putHole(holes, 300, 570);
        this.putHole(holes, 2340 - 300, 570);

        this.putHole(holes, 310, 960);
        this.putHole(holes, 2340 - 310, 960);

        this.putObstacle(obstacles, 500, 570, 200, 30);
        this.putObstacle(obstacles, 2340 - 700, 570, 200, 30);

        for (int i = 0; i < 3; i++) {
            this.putObstacle(obstacles, 490, 320 + 70 * i, 30, 20);
            this.putObstacle(obstacles, 2340 - 520, 320 + 70 * i, 30, 20);
        }

        this.putObstacle(obstacles, 670, 221, 30, 350);
        this.putObstacle(obstacles, 2340 - 700, 221, 30, 350);

        this.putObstacle(obstacles, 370, 221, 300, 30);
        this.putObstacle(obstacles, 2340 - 670, 221, 300, 30);

        this.putObstacle(obstacles, 370, 221, 30, 700);
        this.putObstacle(obstacles, 2340 - 400, 221, 30, 700);

        for (int i = 0; i < 4; i++) {
            this.putObstacle(obstacles, 370 + 200 * i, 921, 180, 30);
            this.putObstacle(obstacles, 2340 - (370 + 200 * i) - 180, 921, 180, 30);
        }

        this.putObstacle(obstacles, 1155, 0, 30, 921);

        this.putObstacle(obstacles, 710, 300, 150, 200);
        this.putObstacle(obstacles, 2340 - 860, 300, 150, 200);

        this.putObstacle(obstacles, 980, 710, 150, 200);
        this.putObstacle(obstacles, 2340 - 1130, 710, 150, 200);

        this.putObstacle(obstacles, 10, 870, 150, 200);
        this.putObstacle(obstacles, 2340 - 160, 870, 150, 200);

        this.putObstacle(obstacles, 210, 300, 150, 200);
        this.putObstacle(obstacles, 2340 - 360, 300, 150, 200);

        this.putObstacle(obstacles, 10, 10, 150, 200);
        this.putObstacle(obstacles, 2340 - 160, 10, 150, 200);

        level.put("holes", holes);
        level.put("obstacles", obstacles);

        levels.put(level);

    }

    public void putHole(JSONArray holes, int x, int y){
        JSONObject hole = new JSONObject();
        try {

            hole.put("x", x);
            hole.put("y", y);

            holes.put(hole);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void putObstacle(JSONArray obstacles, int x, int y, int width, int height){
        JSONObject obstacle = new JSONObject();
        try {

            obstacle.put("x", x);
            obstacle.put("y", y);
            obstacle.put("width", width);
            obstacle.put("height", height);

            obstacles.put(obstacle);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getLevelNumber() {
        return levels.length();
    }



}
