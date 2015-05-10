package cal.bread.dan.breadcalculator2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;

import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.LinkedHashMap;
import java.util.Set;


public class MainActivity extends ActionBarActivity {
    private LinkedHashMap<String, Integer> breadHM;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private ImageButton macInc, macDec, hamInc, hamDec, sDonutInc, sDonutDec, sPieInc, sPieDec, pizzaInc, pizzaDec;
    private ImageButton stDonutInc, stDonutDec, creamInc, creamDec, sandInc, sandDec, sCupInc, sCupDec, choInc, choDec;
    private ImageButton cCupInc, cCupDec, bCCInc, bCCDec, cCakeInc, cCakeDec, rDonutInc, rDonutDec, croInc, croDec;
    private ImageButton sWrapInc, sWrapDec, jRollInc, jRollDec, breadInc, breadDec, hDogInc, hDogDec, cDonutInc, cDonutDec;
    private ImageButton mBreadInc, mBreadDec, sBreadInc, sBreadDec, donutInc, donutDec;
    private TextView macCount, hamCount, sDonutCount, sPieCount, pizzaCount, cCupCount, bCCCount, cCakeCount, rDonutCount, croCount;
    private TextView stDonutCount, creamCount, sandCount, sCupCount, choCount, sWrapCount, jRollCount, breadCount, hDogCount, cDonutCount;
    private TextView mBreadCount, sBreadCount, donutCount;
    private TextView iniTrain, goalTrain, iniStar, goalStar;
    private Button reset, optimize, consume;
    //TextView startTrain, startStar, endTrain, endStar;
    private int startStarInt = -1, endStarInt = -1, startTrainInt = -1, endTrainInt = -1;
    private TrainingList tList;
    private ImageView bread1, bread2, bread3, bread4, bread5, bread0, listStar;
    private ImageButton lastList, nextList;
    private ArrayList<ImageView> breadImages;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        breadHM = setupHashMap();
        updateBreadHM(breadHM);
        initializeButtons();
        setListeners();
        /*
        endTrainInt = Integer.parseInt("7900");
        startTrainInt = Integer.parseInt("0");
        endStarInt = Integer.parseInt("5");
        startStarInt = Integer.parseInt("5");
        */
        breadImages = new ArrayList<>(6);
        breadImages.add(bread0);
        breadImages.add(bread1);
        breadImages.add(bread2);
        breadImages.add(bread3);
        breadImages.add(bread4);
        breadImages.add(bread5);


    }

    private void createAlerts(){
        AlertDialog.Builder alertSS = new AlertDialog.Builder(this);
        final EditText inputSS = new EditText(this);
        alertSS.setView(inputSS);
        alertSS.setMessage("Set Current Hero Star");
        inputSS.setFilters(new InputFilter[]{new InputFilterMinMax(1, 6)});
        inputSS.setInputType(0x00000002);
        alertSS.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //startStar.setText("Current Star: " + inputSS.getText());
                startStarInt = Integer.parseInt(inputSS.getText().toString());
            }
        });
        alertSS.show();
        AlertDialog.Builder alertES = new AlertDialog.Builder(this);
        final EditText inputES = new EditText(this);
        inputES.setFilters(new InputFilter[]{new InputFilterMinMax(1,6)});
        inputES.setInputType(0x00000002);
        alertES.setMessage("Set Goal Hero Star");
        alertES.setView(inputES);
        alertES.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //endStar.setText("Goal Star: " + inputES.getText());
                endStarInt = Integer.parseInt(inputES.getText().toString());
            }
        });
        alertES.show();
        AlertDialog.Builder alertST = new AlertDialog.Builder(this);
        final EditText inputST = new EditText(this);
        inputST.setInputType(0x00000002);
        alertST.setMessage("Set Starting Hero Train");
        alertST.setView(inputST);
        alertST.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
               // startTrain.setText("Start Train: " + inputST.getText());
                startTrainInt = Integer.parseInt(inputST.getText().toString());
            }
        });
        alertST.show();
        AlertDialog.Builder alertET = new AlertDialog.Builder(this);
        final EditText inputET = new EditText(this);
        inputET.setInputType(0x00000002);
        alertET.setMessage("Set Goal Hero Train");
        alertET.setView(inputET);
        alertET.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //endTrain.setText("Goal Train: " + inputET.getText());
                endTrainInt = Integer.parseInt(inputET.getText().toString());
            }
        });
        alertET.show();
    }
    private void printBreadAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        String message = "";
        if(tList != null) {
            int cost = 0;
            int train = 0;
            for (BreadList bList : tList.getLists()) {
                message += bList.size;
                for (Bread bread : bList.getBreads()) {
                    message += "|" + bread.getName() + "|";
                }
                cost += bList.getCost();
                train += bList.getTrain();
                message +="|Cost: "+ cost + " |Train: " + train + "\n\n";
            }
        } else {
            message = "Not Enough Bread to Reach";
        }
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void updateBreadHM(LinkedHashMap<String, Integer> breadHM){
        Set<String> breads = breadHM.keySet();
        for(String name : breads) {
            breadHM.put(name, sharedPref.getInt(name,0));
        }
    }
    private LinkedHashMap<String, Integer> setupHashMap(){
        LinkedHashMap<String, Integer> breadHM = new LinkedHashMap<>();
        breadHM.put("Macaroon", 0);
        breadHM.put("Hamburger", 0);
        breadHM.put("Special Donut", 0);
        breadHM.put("Strawberry Pie", 0);
        breadHM.put("Chocolate", 0);
        breadHM.put("Pizza", 0);
        breadHM.put("Choco Cup Cake", 0);
        breadHM.put("Shamrock Cup Cake", 0);
        breadHM.put("Strawberry Donut", 0);
        breadHM.put("Cream Bread", 0);
        breadHM.put("Christmas Cake", 0);
        breadHM.put("Sandwich", 0);
        breadHM.put("Big Choco Cake", 0);
        breadHM.put("Rice Donut", 0);
        breadHM.put("Croissant", 0);
        breadHM.put("Snake Wrap", 0);
        breadHM.put("Jelly Roll", 0);
        breadHM.put("Bread", 0);
        breadHM.put("Hot Dog", 0);
        breadHM.put("Morning Bread", 0);
        breadHM.put("Choco Donut", 0);
        breadHM.put("Sausage Bread", 0);
        breadHM.put("Donut", 0);
        return breadHM;
    }
    private void initializeButtons(){
        consume = (Button) findViewById(R.id.consume);
        listStar = (ImageView) findViewById(R.id.listStar);
        reset = (Button) findViewById(R.id.resetButton);
        optimize = (Button) findViewById(R.id.optimize);
        nextList = (ImageButton) findViewById(R.id.nextList);
        lastList = (ImageButton) findViewById(R.id.lastList);
        bread0 = (ImageView) findViewById(R.id.bread0);
        bread1 = (ImageView) findViewById(R.id.bread1);
        bread2 = (ImageView) findViewById(R.id.bread2);
        bread3 = (ImageView) findViewById(R.id.bread3);
        bread4 = (ImageView) findViewById(R.id.bread4);
        bread5 = (ImageView) findViewById(R.id.bread5);
        iniStar = (TextView) findViewById(R.id.setIniStar);
        iniTrain = (TextView) findViewById(R.id.setIniTrain);
        goalTrain = (TextView) findViewById(R.id.setGoalTrain);
        goalStar = (TextView) findViewById(R.id.setGoalStar);
        macInc = (ImageButton) findViewById(R.id.macaroonInc);
        macDec = (ImageButton) findViewById(R.id.macaroonDec);
        macCount = (TextView) findViewById(R.id.MacaroonCount);
        macCount.setText(Integer.toString(sharedPref.getInt("Macaroon", 0)));
        hamInc = (ImageButton) findViewById(R.id.hamburgerInc);
        hamDec = (ImageButton) findViewById(R.id.hamburgerDec);
        hamCount = (TextView) findViewById(R.id.HamburgerCount);
        hamCount.setText(Integer.toString(sharedPref.getInt("Hamburger", 0)));
        sDonutInc = (ImageButton) findViewById(R.id.sDonutInc);
        sDonutDec = (ImageButton) findViewById(R.id.sDonutDec);
        sDonutCount = (TextView) findViewById(R.id.SpecialDonutCount);
        sDonutCount.setText(Integer.toString(sharedPref.getInt("Special Donut", 0)));
        sPieInc = (ImageButton) findViewById(R.id.sPieInc);
        sPieDec = (ImageButton) findViewById(R.id.sPieDec);
        sPieCount = (TextView) findViewById(R.id.StrawberryPieCount);
        sPieCount.setText(Integer.toString(sharedPref.getInt("Strawberry Pie", 0)));
        pizzaInc = (ImageButton) findViewById(R.id.pizzaInc);
        pizzaDec = (ImageButton) findViewById(R.id.pizzaDec);
        pizzaCount = (TextView) findViewById(R.id.PizzaCount);
        pizzaCount.setText(Integer.toString(sharedPref.getInt("Pizza", 0)));
        stDonutInc = (ImageButton) findViewById(R.id.stDonutInc);
        stDonutDec = (ImageButton) findViewById(R.id.stDonutDec);
        stDonutCount = (TextView) findViewById(R.id.StrawberryDonutCount);
        stDonutCount.setText(Integer.toString(sharedPref.getInt("Strawberry Donut", 0)));
        creamInc = (ImageButton) findViewById(R.id.cBreadInc);
        creamDec = (ImageButton) findViewById(R.id.cBreadDec);
        creamCount = (TextView) findViewById(R.id.CreamBreadCount);
        creamCount.setText(Integer.toString(sharedPref.getInt("Cream Bread", 0)));
        sandInc = (ImageButton) findViewById(R.id.sandwichInc);
        sandDec = (ImageButton) findViewById(R.id.sandiwchDec);
        sandCount = (TextView) findViewById(R.id.SandwichCount);
        sandCount.setText(Integer.toString(sharedPref.getInt("Sandwich", 0)));
        sCupInc = (ImageButton) findViewById(R.id.sCupCakeInc);
        sCupDec = (ImageButton) findViewById(R.id.sCupCakeDec);
        sCupCount = (TextView) findViewById(R.id.ShamrockCupCakeCount);
        sCupCount.setText(Integer.toString(sharedPref.getInt("Shamrock Cup Cake", 0)));
        choInc = (ImageButton) findViewById(R.id.chocolateInc);
        choDec = (ImageButton) findViewById(R.id.chocolateDec);
        choCount = (TextView) findViewById(R.id.ChocolateCount);
        choCount.setText(Integer.toString(sharedPref.getInt("Chocolate", 0)));
        cCupInc = (ImageButton) findViewById(R.id.cCupCakeInc);
        cCupDec = (ImageButton) findViewById(R.id.cCupCakeDec);
        cCupCount = (TextView) findViewById(R.id.ChocoCupCakeCount);
        cCupCount.setText(Integer.toString(sharedPref.getInt("Choco Cup Cake", 0)));
        bCCInc = (ImageButton) findViewById(R.id.bCCInc);
        bCCDec = (ImageButton) findViewById(R.id.bCCDec);
        bCCCount = (TextView) findViewById(R.id.BigChocecCakeCount);
        bCCCount.setText(Integer.toString(sharedPref.getInt("Big Chocec Cake", 0)));
        cCakeInc = (ImageButton) findViewById(R.id.cCakeInc);
        cCakeDec = (ImageButton) findViewById(R.id.cCakeDec);
        cCakeCount = (TextView) findViewById(R.id.ChristmasCakeCount);
        cCakeCount.setText(Integer.toString(sharedPref.getInt("Christmas Cake", 0)));
        rDonutInc = (ImageButton) findViewById(R.id.rDonutInc);
        rDonutDec = (ImageButton) findViewById(R.id.rDonutDec);
        rDonutCount = (TextView) findViewById(R.id.RiceDonutCount);
        rDonutCount.setText(Integer.toString(sharedPref.getInt("Rice Donut", 0)));
        croInc = (ImageButton) findViewById(R.id.croInc);
        croDec = (ImageButton) findViewById(R.id.croDec);
        croCount = (TextView) findViewById(R.id.CroissantCount);
        croCount.setText(Integer.toString(sharedPref.getInt("Croissant", 0)));
        sWrapInc = (ImageButton) findViewById(R.id.sWrapInc);
        sWrapDec = (ImageButton) findViewById(R.id.sWrapDec);
        sWrapCount = (TextView) findViewById(R.id.SnackWrapCount);
        sWrapCount.setText(Integer.toString(sharedPref.getInt("Snack Wrap", 0)));
        jRollInc = (ImageButton) findViewById(R.id.jRollInc);
        jRollDec = (ImageButton) findViewById(R.id.jRollDec);
        jRollCount = (TextView) findViewById(R.id.JellyRollCount);
        jRollCount.setText(Integer.toString(sharedPref.getInt("Jelly Roll", 0)));
        breadInc = (ImageButton) findViewById(R.id.breadInc);
        breadDec = (ImageButton) findViewById(R.id.breadDec);
        breadCount = (TextView) findViewById(R.id.BreadCount);
        breadCount.setText(Integer.toString(sharedPref.getInt("Bread", 0)));
        hDogInc = (ImageButton) findViewById(R.id.hDogInc);
        hDogDec = (ImageButton) findViewById(R.id.hDogDec);
        hDogCount = (TextView) findViewById(R.id.HotDogCount);
        hDogCount.setText(Integer.toString(sharedPref.getInt("Hot Dog", 0)));
        cDonutInc = (ImageButton) findViewById(R.id.cDonutInc);
        cDonutDec = (ImageButton) findViewById(R.id.cDonutDec);
        cDonutCount = (TextView) findViewById(R.id.ChocoDonutCount);
        cDonutCount.setText(Integer.toString(sharedPref.getInt("Choco Donut", 0)));
        mBreadInc = (ImageButton) findViewById(R.id.mBreadInc);
        mBreadDec = (ImageButton) findViewById(R.id.mBreadDec);
        mBreadCount = (TextView) findViewById(R.id.MorningBreadCount);
        mBreadCount.setText(Integer.toString(sharedPref.getInt("Morning Bread", 0)));
        sBreadInc = (ImageButton) findViewById(R.id.sBreadInc);
        sBreadDec = (ImageButton) findViewById(R.id.sBreadDec);
        sBreadCount = (TextView) findViewById(R.id.SausageBreadCount);
        sBreadCount.setText(Integer.toString(sharedPref.getInt("Sausage Bread", 0)));
        donutInc = (ImageButton) findViewById(R.id.donutInc);
        donutDec = (ImageButton) findViewById(R.id.donutDec);
        donutCount = (TextView) findViewById(R.id.DonutCount);
        donutCount.setText(Integer.toString(sharedPref.getInt("Donut", 0)));

    }
    private void setBreadImage(int index){
        BreadList bList = tList.getLists().get(index);
        int size = bList.getSize();
        int star = bList.getStar();
        listStar.setImageResource(getResources().getIdentifier("drawable/star"+star, null, getPackageName()));
        for(int i = 0; i<size;i++){
            Bread bread = bList.getBread(i);
            breadImages.get(i).setImageResource(getResources().getIdentifier("drawable/"+bread.getName().toLowerCase().replace(" ", ""), null,getPackageName()));
        }
        for(int j = size; j < 6; j++){
            breadImages.get(j).setImageResource(getResources().getIdentifier("drawable/empty", null, getPackageName()));
        }
    }
    private TextView getTextView(String name){
        TextView tv;
        switch(name) {
            case "Macaroon":
                tv = macCount;
                break;
            case "Strawberry Pie":
                tv = sPieCount;
                break;
            case "Cream Bread":
                tv = creamCount;
                break;
            case "Croissant":
                tv = croCount;
                break;
            case "Bread":
                tv = breadCount;
                break;
            case "Morning Bread":
                tv = mBreadCount;
                break;
            case "Chocolate":
                tv = choCount;
                break;
            case "Choco Cup Cake":
                tv = cCupCount;
                break;
            case "Sausage Bread":
                tv = sBreadCount;
                break;
            case "Hot Dog":
                tv = hDogCount;
                break;
            case "Snake Wrap":
                tv = sWrapCount;
                break;
            case "Sandwich":
                tv = sandCount;
                break;
            case "Pizza":
                tv = pizzaCount;
                break;
            case "Hamburger":
                tv = hamCount;
                break;
            case "Christmas Cake":
                tv = cCakeCount;
                break;
            case "Shamrock Cup Cake":
                tv = sCupCount;
                break;
            case "Donut":
                tv = donutCount;
                break;
            case "Choco Donut":
                tv = cDonutCount;
                break;
            case "Jelly Roll":
                tv = jRollCount;
                break;
            case "Rice Donut":
                tv = rDonutCount;
                break;
            case "Strawberry Donut":
                tv = stDonutCount;
                break;
            case "Special Donut":
                tv = sDonutCount;
                break;
            case "Big Chocec Cake":
                tv = bCCCount;
                break;
            default:
                throw new IllegalArgumentException("Invalid bread name: " + name);
        }
        return tv;
    }
    public void optimizeAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Please Enter Required Field");
        alert.setMessage("Press the text to enter Initial Train, Goal Train, Initial Star, and Goal Star for your hero.");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }
    private void setListeners(){
        consume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkedList<BreadList> bLists = tList.getLists();
                BreadList bList = bLists.get(index);
                for(Bread bread : bList.getBreads()){
                    breadHM.put(bread.getName(), breadHM.get(bread.getName()) - 1);
                    editor.putInt(bread.getName(), breadHM.get(bread.getName()));
                    TextView tv = getTextView(bread.getName());
                    tv.setText(Integer.toString(breadHM.get(bread.getName())));
                }
                editor.commit();
                updateBreadHM(breadHM);
                bLists.remove(index);
                if(index != 0) {
                    index--;
                }
                if(tList.getLists().size() != 0) {
                    setBreadImage(index);
                } else {
                    for(ImageView bImage: breadImages){
                        bImage.setImageResource(0);
                    }
                    listStar.setImageResource(0);
                    lastList.setImageResource(0);
                    nextList.setImageResource(0);
                }
            }
        });
        nextList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tList != null && index < tList.getLists().size() - 1) {
                    index++;
                    setBreadImage(index);
                }
            }
        });
        lastList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index > 0 && tList != null){
                    index--;
                    setBreadImage(index);
                }
            }
        });
        optimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startStarInt == -1 || endStarInt == -1 || startTrainInt == -1 || endStarInt == -1) {
                   optimizeAlert();
                } else {
                    updateBreadHM(breadHM);
                    ArrayList<Integer> goal = new ArrayList(4);
                    goal.add(startStarInt);
                    goal.add(endStarInt);
                    goal.add(startTrainInt);
                    goal.add(endTrainInt);
                    BreadOptimizer optimizer = new BreadOptimizer(breadHM, goal);
                    tList = optimizer.optimize();
                    printBreadAlert();
                    index = 0;
                    if (tList != null) {
                        setBreadImage(index);
                    }
                    lastList.setBackgroundResource(getResources().getIdentifier("drawable/bigleftarrow", null, getPackageName()));
                    nextList.setBackgroundResource(getResources().getIdentifier("drawable/bigrightarrow", null, getPackageName()));
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                breadHM = setupHashMap();
                for(String name: breadHM.keySet()){
                    editor.putInt(name, 0);
                }
                editor.commit();
                initializeButtons();
            }
        });
        macInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int macaroonCount = sharedPref.getInt("Macaroon", 0);
                macaroonCount++;
                editor.putInt("Macaroon", macaroonCount);
                editor.commit();
                macCount.setText(Integer.toString(macaroonCount));
            }
        });
        macDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int macaroonCount = sharedPref.getInt("Macaroon", 0);
                if(macaroonCount > 0) {
                    macaroonCount--;
                    editor.putInt("Macaroon", macaroonCount);
                    editor.commit();
                    macCount.setText(Integer.toString(macaroonCount));
                }
            }
        });
        hamInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Hamburger", 0);
                count++;
                editor.putInt("Hamburger", count);
                editor.commit();
                hamCount.setText(Integer.toString(count));
            }
        });
        hamDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Hamburger", 0);
                if(count > 0) {
                    count--;
                    editor.putInt("Hamburger", count);
                    editor.commit();
                    hamCount.setText(Integer.toString(count));
                }
            }
        });
        sDonutInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Special Donut", 0);
                count++;
                editor.putInt("Special Donut", count);
                editor.commit();
                sDonutCount.setText(Integer.toString(count));
            }
        });
        sDonutDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Special Donut", 0);
                if(count > 0) {
                    count--;
                    editor.putInt("Special Donut", count);
                    editor.commit();
                    sDonutCount.setText(Integer.toString(count));
                }
            }
        });
        sPieInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Strawberry Pie", 0);
                count++;
                editor.putInt("Strawberry Pie", count);
                editor.commit();
                sPieCount.setText(Integer.toString(count));
            }
        });
        sPieDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Strawberry Pie", 0);
                if(count > 0) {
                    count--;
                    editor.putInt("Strawberry Pie", count);
                    editor.commit();
                    sPieCount.setText(Integer.toString(count));
                }
            }
        });
        sPieInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Strawberry Pie", 0);
                count++;
                editor.putInt("Strawberry Pie", count);
                editor.commit();
                sPieCount.setText(Integer.toString(count));
            }
        });
        sPieDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Strawberry Pie", 0);
                if(count > 0) {
                    count--;
                    editor.putInt("Strawberry Pie", count);
                    editor.commit();
                    sPieCount.setText(Integer.toString(count));
                }
            }
        });
        pizzaInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Pizza", 0);
                count++;
                editor.putInt("Pizza", count);
                editor.commit();
                pizzaCount.setText(Integer.toString(count));
            }
        });
        pizzaDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Pizza", 0);
                if (count > 0) {
                    count--;
                    editor.putInt("Pizza", count);
                    editor.commit();
                    pizzaCount.setText(Integer.toString(count));
                }
            }
        });
        stDonutInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Strawberry Donut", 0);
                count++;
                editor.putInt("Strawberry Donut", count);
                editor.commit();
                stDonutCount.setText(Integer.toString(count));
            }
        });
        stDonutDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Strawberry Donut", 0);
                if (count > 0) {
                    count--;
                    editor.putInt("Strawberry Donut", count);
                    editor.commit();
                    stDonutCount.setText(Integer.toString(count));
                }
            }
        });
        creamInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Cream Bread", 0);
                count++;
                editor.putInt("Cream Bread", count);
                editor.commit();
                creamCount.setText(Integer.toString(count));
            }
        });
        creamDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Cream Bread", 0);
                if (count > 0) {
                    count--;
                    editor.putInt("Cream Bread", count);
                    editor.commit();
                    creamCount.setText(Integer.toString(count));
                }
            }
        });
        sandInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Sandwich", 0);
                count++;
                editor.putInt("Sandwich", count);
                editor.commit();
                sandCount.setText(Integer.toString(count));
            }
        });
        sandDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Sandwich", 0);
                if (count > 0) {
                    count--;
                    editor.putInt("Sandwich", count);
                    editor.commit();
                    sandCount.setText(Integer.toString(count));
                }
            }
        });
        sCupInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Shamrock Cup Cake", 0);
                count++;
                editor.putInt("Shamrock Cup Cake", count);
                editor.commit();
                sCupCount.setText(Integer.toString(count));
            }
        });
        sCupDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Shamrock Cup Cake", 0);
                if (count > 0) {
                    count--;
                    editor.putInt("Shamrock Cup Cake", count);
                    editor.commit();
                    sCupCount.setText(Integer.toString(count));
                }
            }
        });
        choInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Chocolate", 0);
                count++;
                editor.putInt("Chocolate", count);
                editor.commit();
                choCount.setText(Integer.toString(count));
            }
        });
        choDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Chocolate", 0);
                if (count > 0) {
                    count--;
                    editor.putInt("Chocolate", count);
                    editor.commit();
                    choCount.setText(Integer.toString(count));
                }
            }
        });
        cCupInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Choco Cup Cake", 0);
                count++;
                editor.putInt("Choco Cup Cake", count);
                editor.commit();
                cCupCount.setText(Integer.toString(count));
            }
        });
        cCupDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Choco Cup Cake", 0);
                if (count > 0) {
                    count--;
                    editor.putInt("Choco Cup Cake", count);
                    editor.commit();
                    cCupCount.setText(Integer.toString(count));
                }
            }
        });
        bCCInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Big Chocec Cake", 0);
                count++;
                editor.putInt("Big Chocec Cake", count);
                editor.commit();
                bCCCount.setText(Integer.toString(count));
            }
        });
        bCCDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Big Chocec Cake", 0);
                if (count > 0) {
                    count--;
                    editor.putInt("Big Chocec Cake", count);
                    editor.commit();
                    bCCCount.setText(Integer.toString(count));
                }
            }
        });
        cCakeInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Christmas Cake", 0);
                count++;
                editor.putInt("Christmas Cake", count);
                editor.commit();
                cCakeCount.setText(Integer.toString(count));
            }
        });
        cCakeDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Christmas Cake", 0);
                if (count > 0) {
                    count--;
                    editor.putInt("Christmas Cake", count);
                    editor.commit();
                    cCakeCount.setText(Integer.toString(count));
                }
            }
        });
        rDonutInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Rice Donut", 0);
                count++;
                editor.putInt("Rice Donut", count);
                editor.commit();
                rDonutCount.setText(Integer.toString(count));
            }
        });
        rDonutDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Rice Donut", 0);
                if (count > 0) {
                    count--;
                    editor.putInt("Rice Donut", count);
                    editor.commit();
                    rDonutCount.setText(Integer.toString(count));
                }
            }
        });
        croInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Croissant", 0);
                count++;
                editor.putInt("Croissant", count);
                editor.commit();
                croCount.setText(Integer.toString(count));
            }
        });
        croDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Croissant", 0);
                if (count > 0) {
                    count--;
                    editor.putInt("Croissant", count);
                    editor.commit();
                    croCount.setText(Integer.toString(count));
                }
            }
        });
        sWrapInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Snack Wrap", 0);
                count++;
                editor.putInt("Snack Wrap", count);
                editor.commit();
                sWrapCount.setText(Integer.toString(count));
            }
        });
        sWrapDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Snack Wrap", 0);
                if (count > 0) {
                    count--;
                    editor.putInt("Snack Wrap", count);
                    editor.commit();
                    sWrapCount.setText(Integer.toString(count));
                }
            }
        });
        jRollInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Jelly Roll", 0);
                count++;
                editor.putInt("Jelly Roll", count);
                editor.commit();
                jRollCount.setText(Integer.toString(count));
            }
        });
        jRollDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Jelly Roll", 0);
                if (count > 0) {
                    count--;
                    editor.putInt("Jelly Roll", count);
                    editor.commit();
                    jRollCount.setText(Integer.toString(count));
                }
            }
        });
        breadInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Bread", 0);
                count++;
                editor.putInt("Bread", count);
                editor.commit();
                breadCount.setText(Integer.toString(count));
            }
        });
        breadDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Bread", 0);
                if (count > 0) {
                    count--;
                    editor.putInt("Bread", count);
                    editor.commit();
                    breadCount.setText(Integer.toString(count));
                }
            }
        });
        hDogInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Hot Dog", 0);
                count++;
                editor.putInt("Hot Dog", count);
                editor.commit();
                hDogCount.setText(Integer.toString(count));
            }
        });
        hDogDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Hot Dog", 0);
                if (count > 0) {
                    count--;
                    editor.putInt("Hot Dog", count);
                    editor.commit();
                    hDogCount.setText(Integer.toString(count));
                }
            }
        });
        cDonutInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Choco Donut", 0);
                count++;
                editor.putInt("Choco Donut", count);
                editor.commit();
                cDonutCount.setText(Integer.toString(count));
            }
        });
        cDonutDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Choco Donut", 0);
                if (count > 0) {
                    count--;
                    editor.putInt("Choco Donut", count);
                    editor.commit();
                    cDonutCount.setText(Integer.toString(count));
                }
            }
        });
        mBreadInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Morning Bread", 0);
                count++;
                editor.putInt("Morning Bread", count);
                editor.commit();
                mBreadCount.setText(Integer.toString(count));
            }
        });
        mBreadDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Morning Bread", 0);
                if (count > 0) {
                    count--;
                    editor.putInt("Morning Bread", count);
                    editor.commit();
                    mBreadCount.setText(Integer.toString(count));
                }
            }
        });
        sBreadInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Sausage Bread", 0);
                count++;
                editor.putInt("Sausage Bread", count);
                editor.commit();
                sBreadCount.setText(Integer.toString(count));
            }
        });
        sBreadDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Sausage Bread", 0);
                if (count > 0) {
                    count--;
                    editor.putInt("Sausage Bread", count);
                    editor.commit();
                    sBreadCount.setText(Integer.toString(count));
                }
            }
        });
        donutInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Donut", 0);
                count++;
                editor.putInt("Donut", count);
                editor.commit();
                donutCount.setText(Integer.toString(count));
            }
        });
        donutDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = sharedPref.getInt("Donut", 0);
                if (count > 0) {
                    count--;
                    editor.putInt("Donut", count);
                    editor.commit();
                    donutCount.setText(Integer.toString(count));
                }
            }
        });
    }
    public void setInitialTrain(final View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Set Initial Train");
        alert.setMessage("Enter the initial Train for the hero");
        final EditText inputText = new EditText(this);
        inputText.setInputType(0x00000002);
        alert.setView(inputText);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String value = inputText.getText().toString();
                try {
                    startTrainInt = Integer.parseInt(value);
                    TextView tv = (TextView) v;
                    tv.setText("Initial Train : " + Integer.toString(startTrainInt));
                } catch (NumberFormatException nfe) {

                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }
    public void setGoalTrain(final View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Set Goal Train");
        alert.setMessage("Enter the Goal Train for the hero");
        final EditText inputText = new EditText(this);
        inputText.setInputType(0x00000002);
        alert.setView(inputText);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            final String value = inputText.getText().toString();
            try {
                endTrainInt = Integer.parseInt(value);
                TextView tv = (TextView) v;
                tv.setText("Goal Train   : " + Integer.toString(endTrainInt));
            } catch (NumberFormatException nfe){

            }
        }
    });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }
    public void setInitialStar(final View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Set Initial Star");
        alert.setMessage("Enter the initial star of the hero");
        final EditText inputText = new EditText(this);
        alert.setView(inputText);
        inputText.setFilters(new InputFilter[]{new InputFilterMinMax(1, 6)});
        inputText.setInputType(0x00000002);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String value = inputText.getText().toString();
                try{
                    startStarInt = Integer.parseInt(value);
                    TextView tv = (TextView) v;
                    tv.setText("Initial Star : " + Integer.toString(startStarInt));
                } catch (NumberFormatException nfe){

                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();

    }
    public void setGoalStar(final View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Set Goal Star");
        alert.setMessage("Enter the Goal star of the hero");
        final EditText inputText = new EditText(this);
        alert.setView(inputText);
        inputText.setFilters(new InputFilter[]{new InputFilterMinMax(1, 6)});
        inputText.setInputType(0x00000002);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String value = inputText.getText().toString();
                try {
                    endStarInt = Integer.parseInt(value);
                    TextView tv = (TextView) v;
                    tv.setText("Goal Star   : " + Integer.toString(endStarInt));
                } catch (NumberFormatException nfe) {

                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }
}
