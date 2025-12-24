import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        // Основные переменные для файлов, директория, префикс, список входных файлов, режим добавление и тип статистики фулл или нет
        String directory = ".";
        String prefix = "";
        boolean append = false;
        boolean full = false;
        List<String> inputs = new ArrayList<>();
        //цикл переборки аргументов, если встречается опция - то идёт обработка, если нет то нет
        for (int i = 0; i < args.length; i++) {
            if ("-o".equals(args[i])) {
                directory = args[++i];
            } else if ("-p".equals(args[i])) {
                prefix = args[++i];
            } else if ("-a".equals(args[i])) {
                append = true;
            } else if ("-f".equals(args[i])) {
                full = true;
            } else if ("-s".equals(args[i])) {
                full = false;
            } else {
                inputs.add(args[i]);
            }
        }
        //Переменные для статистики
        long integerCount = 0, floatCount = 0, stringCount = 0; //кол-во типов данных
        double integerSum = 0, integerMin = 0, integerMax = 0; //сумма, мин и макс числовых
        double floatSum = 0, floatMin = 0, floatMax = 0; //сумма, мин и макс дробных
        int stringMin = 0, stringMax = 0; //сумма, мин и макс строковых
        boolean integerFirst = true, floatFirst = true, stringFirst = true; //первые значения
        BufferedWriter integerWrite = null, floatWrite = null, stringWrite = null; //для записи потоки

        try {
            for (String name : inputs) {
                try (BufferedReader reader = new BufferedReader(new FileReader(name))) {

                    String line;
                    while ((line = reader.readLine()) != null) {

                        // обработка циклом числовые значения
                        try {
                            long value = Long.parseLong(line);

                            if (integerWrite == null)
                                integerWrite = writer(directory, prefix + "integers.txt", append);
                            integerWrite.write(line);
                            integerWrite.newLine();
                            if (integerFirst) {
                                integerMin = integerMax = value;
                                integerFirst = false;
                            }
                            integerCount++;
                            integerSum += value;
                            integerMin = Math.min(integerMin, value);
                            integerMax = Math.max(integerMax, value);
                            continue;
                        } catch (NumberFormatException ignored) {}
                        // обработка циклом дробные значения
                        try {
                            double value = Double.parseDouble(line);

                            if (floatWrite == null)
                                floatWrite = writer(directory, prefix + "floats.txt", append);
                            floatWrite.write(line);
                            floatWrite.newLine();
                            if (floatFirst) {
                                floatMin = floatMax = value;
                                floatFirst = false;
                            }
                            floatCount++;
                            floatSum += value;
                            floatMin = Math.min(floatMin, value);
                            floatMax = Math.max(floatMax, value);
                            continue;
                        } catch (NumberFormatException ignored) {}
                        // тут уже строки
                        if (stringWrite == null)
                            stringWrite = writer(directory, prefix + "strings.txt", append);
                        stringWrite.write(line);
                        stringWrite.newLine();
                        int len = line.length();
                        if (stringFirst) {
                            stringMin = stringMax = len;
                            stringFirst = false;
                        }
                        stringCount++;
                        stringMin = Math.min(stringMin, len);
                        stringMax = Math.max(stringMax, len);
                    }
                } catch (IOException e) {
                    System.out.println("ОШИБКА ЧТЕНИЯ ФАЙЛА: " + name);
                }
            }
        }  finally {
            close(integerWrite);
            close(floatWrite);
            close(stringWrite);
        }
        // Вывод статистики
        if (integerCount > 0) {
            System.out.println("Числовых значений кол-во = " + integerCount);
            if (full) {
                System.out.println("Мин. = " + integerMin);
                System.out.println("Макс. = " + integerMax);
                System.out.println("Сумма = " + integerSum);
                System.out.println("Среднее знач. = " + (integerSum / integerCount));
            }
        }
        if (floatCount > 0) {
            System.out.println("Дробных значений кол-во = " + floatCount);
            if (full) {
                System.out.println("Мин. = " + floatMin);
                System.out.println("Макс. = " + floatMax);
                System.out.println("Сумма = " + floatSum);
                System.out.println("Среднее знач. = " + (floatSum / floatCount));
            }
        }
        if (stringCount > 0) {
            System.out.println("Строковых значений кол-во = " + stringCount);
            if (full) {
                System.out.println("Короткие = " + stringMin);
                System.out.println("Длинные = " + stringMax);
            }
        }
    }
    static BufferedWriter writer(String dir, String name, boolean append) throws IOException {
        File d = new File(dir);
        if (!d.exists()) d.mkdirs();
        return new BufferedWriter(new FileWriter(new File(d, name), append));
    }
    //закрытие врайтера для оптимитизации
    static void close(BufferedWriter w) {
        try {
            if (w != null) w.close();
        } catch (IOException ignored) {}
    }
}
