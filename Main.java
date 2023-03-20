import Model.TradingRecord;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class Main {

    static Map<String, Queue<TradingRecord>> recordMap = new HashMap<>();
    static  BigDecimal profit = BigDecimal.ZERO;


    public static void main(String[] args) {
        String file = "./crypto_tax.txt";

        try{
            BufferedReader reader = getReader(file);

            List<TradingRecord> content = readFile(reader);

//            Double sum = calulateProfit(content);

            System.out.println(profit.toString());



        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println(ex);
        }

    }

    static BufferedReader getReader(String path) throws FileNotFoundException {

            BufferedReader reader = new BufferedReader(new FileReader(path));
            return reader;

    }

    static List<TradingRecord> readFile(BufferedReader reader) throws IOException {


        List<TradingRecord> content = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            String[] lineSplit = line.trim().split(" ");
            TradingRecord record = new TradingRecord();
            record.setCoinType(lineSplit[1]);
            record.setValue(BigDecimal.valueOf(Double.parseDouble(lineSplit[2])));
            record.setAmount(BigDecimal.valueOf(Double.parseDouble(lineSplit[3])));


            if(Objects.equals(lineSplit[0], "B") ){
                buyCoin(record);
            } else if(Objects.equals(lineSplit[0], "S")){
                sellCoin(record);
            }

//            System.out.println(recordMap);
//            System.out.println("profit  " + profit);



            content.add(record);
        }

        return content;
    }

    static void buyCoin(TradingRecord record){
        if(recordMap.containsKey(record.getCoinType())){
            Queue<TradingRecord> recordQueue = recordMap.get(record.getCoinType());
            recordQueue.add(record);
            recordMap.put(record.getCoinType(), recordQueue);
        }
        else {
            Queue<TradingRecord> recordQueue = new LinkedList<>();
            recordQueue.add(record);
            recordMap.put(record.getCoinType(), recordQueue);

        }
    }

    static void sellCoin(TradingRecord newRecord)throws  RuntimeException{
        if(!recordMap.containsKey(newRecord.getCoinType())){
            throw new IllegalStateException("Coin Not Found");
        }

        Queue<TradingRecord> recordQueue = recordMap.get(newRecord.getCoinType());

        if(recordQueue.isEmpty()){
            throw new IllegalStateException("Coin Not Sufficient");
        }


        TradingRecord record = peekQueue(recordQueue);

        if(record.getAmount().compareTo(newRecord.getAmount()) == 0){
            BigDecimal profitPerCoin = newRecord.getValue().subtract(record.getValue());
            profit = profit.add(record.getAmount().multiply(profitPerCoin));
            recordQueue.poll();

        } else if(record.getAmount().compareTo(newRecord.getAmount()) > 0) {
            BigDecimal profitPerCoin = newRecord.getValue().subtract(record.getValue());
//            System.out.println("profitPerCoin " + profitPerCoin);
            profit = profit.add(newRecord.getAmount().multiply(profitPerCoin));
//            System.out.println("profit " + profit);
            BigDecimal newAmount = record.getAmount().subtract(newRecord.getAmount());
            record.setAmount(newAmount);

//            System.out.println("remainingCoin 0 " + record.getAmount());

        }else {
            BigDecimal remainingCoin = newRecord.getAmount();
            while(remainingCoin.compareTo(record.getAmount()) >= 0){

//                System.out.println(newRecord.toString());


                record = recordQueue.poll();
                if(record == null){
                    throw new IllegalStateException("Coin Not Sufficient");
                }

                BigDecimal profitPerCoin = newRecord.getValue().subtract(record.getValue());
//                System.out.println("profitPerCoin " + profitPerCoin);

                profit = profit.add(record.getAmount().multiply(profitPerCoin));

//                System.out.println("profit " + profit);

                remainingCoin = remainingCoin.subtract(record.getAmount());

//                System.out.println("remainingCoin " + remainingCoin);
                if(remainingCoin.compareTo(BigDecimal.ZERO) == 0){
                    return;
                }
                record = peekQueue(recordQueue);
            }

            if(!(remainingCoin.compareTo(BigDecimal.ZERO) == 0)){
                record = peekQueue(recordQueue);

                BigDecimal profitPerCoin = newRecord.getValue().subtract(record.getValue());
                profit = profit.add(remainingCoin.multiply(profitPerCoin));
                BigDecimal newAmount = record.getAmount().subtract(remainingCoin);
                record.setAmount(newAmount);
            }
        }


    }
    private static TradingRecord peekQueue(Queue<TradingRecord> recordQueue) {
        TradingRecord record;
        record = recordQueue.peek();
        if(record == null){
            throw new IllegalStateException("Coin Not Sufficient");
        }
        return record;
    }






}
