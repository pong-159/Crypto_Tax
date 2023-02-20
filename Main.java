import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        String file = "./crypto_tax.txt";

        try{
            BufferedReader reader = getReader(file);

            List<String[]> content = readFile(reader);

            Double sum = calulateProfit(content);

            System.out.println(sum.toString());



        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println(ex);
        }

    }

    static BufferedReader getReader(String path) throws FileNotFoundException {

            BufferedReader reader = new BufferedReader(new FileReader(path));
            return reader;

    }

    static List<String[]> readFile(BufferedReader reader) throws IOException {
        String line;
        List<String[]> content = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            String[] lineSplit = line.trim().split(" ");
            content.add(lineSplit);
        }

        return content;
    }


    static Double calulateProfit(List<String[]> content){


        Double sum = Double.valueOf(0);
        Map<String, List<String[]>> coinMap = new HashMap<>();

        List<String[]> coinDetail = new ArrayList<>();


        
        for(String[] line : content){


//            System.out.println("num " + line[3]);

            if(coinMap.containsKey(line[1])){


                List<String[]> detailList = coinMap.get(line[1]);


                if(Objects.equals(line[0], "B")){
                    detailList.add(line);
                    coinMap.put(line[1],detailList);
                }
                else if(Objects.equals(line[0], "S")){

                    Double totalCoin = Double.valueOf(0);
                    for (String[] l : detailList){
                        totalCoin += Double.valueOf(l[3]);
                    }

//                    System.out.println("detail list " + detailList.size());
//                    detailList.forEach(d -> System.out.println("dd " + d));

                    for(int i = 0; i < detailList.size(); i++){



                        String[] detail = detailList.get(i);


                        Double coinSell = Double.valueOf(line[3]);
                        Double priceSell = Double.valueOf(line[2]);

                        if( totalCoin < coinSell){
                            throw new IllegalStateException("Coin Not Enough");
                        }

                        Double coinHas = Double.valueOf(detail[3]);
                        Double priceHas = Double.valueOf(detail[2]);

                        if(coinSell > coinHas){
                            sum += (priceSell - priceHas) * coinHas;
//                            System.out.println("priceSell   " + priceSell);
//                            System.out.println("priceHas   " + priceHas);
//                            System.out.println("coinHas   " + coinHas);
//
//                            System.out.println("sum 3   " + sum);

                            Double remainingCoin = (coinSell - coinHas);
                            line[3] = remainingCoin.toString();

                            detailList.remove(i);
                            i--;
                        }


                        else if(coinSell < coinHas){
                            sum += (priceSell - priceHas) * coinSell;

//                            System.out.println("priceSell   " + priceSell);
//                            System.out.println("priceHas   " + priceHas);
//                            System.out.println("coinSell   " + coinSell);
//                            System.out.println("sum 1  " + sum);

                            Double remainingCoin = (coinHas - coinSell);
                            detailList.get(i)[3] = remainingCoin.toString();

                            break;

                        }else if(coinHas == coinSell){
                            sum += (priceSell - priceHas) * coinSell;

//                            System.out.println("priceSell   " + priceSell);
//                            System.out.println("priceHas   " + priceHas);
//                            System.out.println("coinHas   " + coinHas);
//
//                            System.out.println("sum 2  " + sum);
                            detailList.remove(i);
                            i--;
                            break;
                        }
                    }

                }

            }
            else {

                if(Objects.equals(line[0], "B")){
                    List<String[]> detailList = new ArrayList<>();
                    detailList.add(line);
                    coinMap.put(line[1], detailList);
                }
                else {
                    throw new IllegalStateException("Coin Not Found");
                }

            }

//            System.out.println("profit " + sum);
        }
        return sum;
    }



}
