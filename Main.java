import functions.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== КОМПЛЕКСНАЯ ПРОВЕРКА РАБОТЫ КЛАССОВ ===\n");

        // Тестирование ArrayTabulatedFunction
        System.out.println("1. ТЕСТИРОВАНИЕ ArrayTabulatedFunction:");
        testImplementation(new ArrayTabulatedFunction(0, 4, 5), "ArrayTabulatedFunction");

        // Тестирование LinkedListTabulatedFunction
        System.out.println("\n\n2. ТЕСТИРОВАНИЕ LinkedListTabulatedFunction:");
        testImplementation(new LinkedListTabulatedFunction(0, 4, 5), "LinkedListTabulatedFunction");

        // Сравнительный тест
        System.out.println("\n\n3. СРАВНИТЕЛЬНЫЙ ТЕСТ ОБЕИХ РЕАЛИЗАЦИЙ:");
        comparativeTest();

        System.out.println("\n=== ВСЕ ТЕСТЫ ЗАВЕРШЕНЫ ===");
    }

    // Полиморфный метод тестирования конкретной реализации
    private static void testImplementation(TabulatedFunction func, String implName) {
        System.out.println("\n--- " + implName + " ---");

        try {
            // Инициализация функции y = x²
            initializeFunction(func);
            System.out.println("Инициализированная функция:");
            func.printFunction();

            // Тестирование корректных операций
            testCorrectOperations(func);

            // Тестирование исключений
            testExceptions(func, implName);

            // Тестирование граничных случаев
            testBoundaryCases(func);

        } catch (Exception e) {
            System.out.println("Непредвиденная ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Инициализация функции значениями y = x²
    private static void initializeFunction(TabulatedFunction func) {
        for (int i = 0; i < func.getPointsCount(); i++) {
            try {
                double x = func.getPointX(i);
                func.setPointY(i, x * x);
            } catch (FunctionPointIndexOutOfBoundsException e) {
                System.out.println("Ошибка инициализации: " + e.getMessage());
            }
        }
    }

    // Тестирование корректных операций
    private static void testCorrectOperations(TabulatedFunction func) {
        System.out.println("\nА) КОРРЕКТНЫЕ ОПЕРАЦИИ:");

        try {
            // Добавление точек
            func.addPoint(new FunctionPoint(1.5, 2.25));
            func.addPoint(new FunctionPoint(3.5, 12.25));
            System.out.println("✓ Добавлены точки (1.5, 2.25) и (3.5, 12.25)");

            // Изменение точек
            func.setPointY(2, 4.0);
            System.out.println("✓ Изменена Y-координата точки с индексом 2 на 4.0");

            // Удаление точки
            func.deletePoint(1);
            System.out.println("✓ Удалена точка с индексом 1");

            // Вычисление значений
            System.out.println("✓ Вычисления функции:");
            double[] testPoints = {0.5, 1.0, 2.0, 2.5, 3.0};
            for (double x : testPoints) {
                double y = func.getFunctionValue(x);
                System.out.printf("    f(%.1f) = %.4f\n", x, y);
            }

            func.printFunction();

        } catch (Exception e) {
            System.out.println("✗ Ошибка в корректных операциях: " + e.getMessage());
        }
    }

    // Тестирование исключений
    private static void testExceptions(TabulatedFunction func, String implName) {
        System.out.println("\nБ) ТЕСТИРОВАНИЕ ИСКЛЮЧЕНИЙ:");

        // 1. FunctionPointIndexOutOfBoundsException
        System.out.println("1. FunctionPointIndexOutOfBoundsException:");
        testIndexOutOfBounds(func);

        // 2. InappropriateFunctionPointException
        System.out.println("2. InappropriateFunctionPointException:");
        testInappropriatePoints(func);

        // 3. IllegalStateException при удалении
        System.out.println("3. IllegalStateException:");
        testIllegalState(func, implName);

        // 4. IllegalArgumentException в конструкторах
        System.out.println("4. IllegalArgumentException в конструкторах:");
        testIllegalArguments();
    }

    // Тестирование выхода за границы индексов
    private static void testIndexOutOfBounds(TabulatedFunction func) {
        int[] invalidIndices = {-1, 10, 100, -5};

        for (int index : invalidIndices) {
            try {
                func.getPoint(index);
                System.out.println("✗ Не выброшено исключение для индекса: " + index);
            } catch (FunctionPointIndexOutOfBoundsException e) {
                System.out.println("✓ FunctionPointIndexOutOfBoundsException для индекса " + index + ": " + e.getMessage());
            } catch (Exception e) {
                System.out.println("✗ Неожиданное исключение для индекса " + index + ": " + e.getMessage());
            }
        }

        // Тестирование setPoint с неверным индексом
        try {
            func.setPoint(10, new FunctionPoint(5.0, 25.0));
            System.out.println("✗ Не выброшено исключение для setPoint с индексом 10");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("✓ FunctionPointIndexOutOfBoundsException для setPoint: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Неожиданное исключение для setPoint: " + e.getMessage());
        }
    }

    // Тестирование некорректных точек
    private static void testInappropriatePoints(TabulatedFunction func) {
        try {
            // Попытка добавить точку с существующим X
            func.addPoint(new FunctionPoint(2.0, 100.0));
            System.out.println("✗ Не выброшено исключение при добавлении точки с существующим X");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("✓ InappropriateFunctionPointException при добавлении дубликата: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Неожиданное исключение при добавлении дубликата: " + e.getMessage());
        }

        try {
            // Попытка изменить точку с нарушением порядка
            func.setPoint(2, new FunctionPoint(0.5, 0.25)); // X меньше предыдущего
            System.out.println("✗ Не выброшено исключение при нарушении порядка X");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("✓ InappropriateFunctionPointException при нарушении порядка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Неожиданное исключение при нарушении порядка: " + e.getMessage());
        }
    }

    // Тестирование IllegalStateException
    private static void testIllegalState(TabulatedFunction func, String implName) {
        // Создаем функцию с минимальным количеством точек
        TabulatedFunction minFunc;
        if (implName.equals("ArrayTabulatedFunction")) {
            minFunc = new ArrayTabulatedFunction(0, 2, 2);
        } else {
            minFunc = new LinkedListTabulatedFunction(0, 2, 2);
        }

        initializeFunction(minFunc);

        try {
            minFunc.deletePoint(0); // Попытка удалить точку при минимальном количестве
            System.out.println("✗ Не выброшено IllegalStateException при удалении из минимальной функции");
        } catch (IllegalStateException e) {
            System.out.println("✓ IllegalStateException при удалении из минимальной функции: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Неожиданное исключение при удалении: " + e.getMessage());
        }
    }

    // Тестирование неверных аргументов конструкторов
    private static void testIllegalArguments() {
        // Неправильные границы
        try {
            TabulatedFunction func = new ArrayTabulatedFunction(5, 0, 3); // leftX > rightX
            System.out.println("✗ Не выброшено IllegalArgumentException для неправильных границ (массив)");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ IllegalArgumentException для неправильных границ (массив): " + e.getMessage());
        }

        try {
            TabulatedFunction func = new LinkedListTabulatedFunction(5, 0, 3); // leftX > rightX
            System.out.println("✗ Не выброшено IllegalArgumentException для неправильных границ (список)");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ IllegalArgumentException для неправильных границ (список): " + e.getMessage());
        }

        // Недостаточное количество точек
        try {
            TabulatedFunction func = new ArrayTabulatedFunction(0, 5, 1); // pointsCount < 2
            System.out.println("✗ Не выброшено IllegalArgumentException для недостаточного количества точек (массив)");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ IllegalArgumentException для недостаточного количества точек (массив): " + e.getMessage());
        }

        try {
            TabulatedFunction func = new LinkedListTabulatedFunction(0, 5, 1); // pointsCount < 2
            System.out.println("✗ Не выброшено IllegalArgumentException для недостаточного количества точек (список)");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ IllegalArgumentException для недостаточного количества точек (список): " + e.getMessage());
        }
    }

    // Тестирование граничных случаев
    private static void testBoundaryCases(TabulatedFunction func) {
        System.out.println("\nВ) ГРАНИЧНЫЕ СЛУЧАИ:");

        try {
            // Точки вне области определения
            System.out.println("Точки вне области определения:");
            double[] outOfDomain = {-1.0, 10.0, -0.0001, 4.0001};
            for (double x : outOfDomain) {
                double y = func.getFunctionValue(x);
                System.out.printf("  f(%.4f) = %s\n", x, Double.isNaN(y) ? "NaN (корректно)" : y);
            }

            // Граничные точки области определения
            System.out.println("Граничные точки области определения:");
            double leftBorder = func.getLeftDomainBorder();
            double rightBorder = func.getRightDomainBorder();
            System.out.printf("  Левая граница: f(%.4f) = %.4f\n", leftBorder, func.getFunctionValue(leftBorder));
            System.out.printf("  Правая граница: f(%.4f) = %.4f\n", rightBorder, func.getFunctionValue(rightBorder));

        } catch (Exception e) {
            System.out.println("Ошибка при тестировании граничных случаев: " + e.getMessage());
        }
    }

    // Сравнительный тест обеих реализаций
    private static void comparativeTest() {
        System.out.println("СРАВНЕНИЕ ArrayTabulatedFunction И LinkedListTabulatedFunction");

        // Создаем обе реализации с одинаковыми параметрами
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(0, 4, 5);
        TabulatedFunction listFunc = new LinkedListTabulatedFunction(0, 4, 5);

        // Инициализируем одинаковыми значениями
        initializeFunction(arrayFunc);
        initializeFunction(listFunc);

        // Проверяем идентичность базовых характеристик
        System.out.println("Базовые характеристики:");
        System.out.printf("  Область определения: [%.2f, %.2f] vs [%.2f, %.2f]%n",
                arrayFunc.getLeftDomainBorder(), arrayFunc.getRightDomainBorder(),
                listFunc.getLeftDomainBorder(), listFunc.getRightDomainBorder());
        System.out.printf("  Количество точек: %d vs %d%n",
                arrayFunc.getPointsCount(), listFunc.getPointsCount());

        // Проверяем идентичность вычислений
        System.out.println("Сравнение вычислений:");
        double[] testPoints = {0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 2.25, 3.75};

        boolean allMatch = true;
        for (double x : testPoints) {
            double y1 = arrayFunc.getFunctionValue(x);
            double y2 = listFunc.getFunctionValue(x);
            boolean match = Math.abs(y1 - y2) < 1e-10 || (Double.isNaN(y1) && Double.isNaN(y2));

            System.out.printf("  f(%.2f): массив=%-10.6f список=%-10.6f совпадают=%-5s%n",
                    x, y1, y2, match ? "ДА" : "НЕТ");

            if (!match) allMatch = false;
        }

        System.out.println("РЕЗУЛЬТАТ СРАВНЕНИЯ: " + (allMatch ? "ВСЕ РЕЗУЛЬТАТЫ СОВПАДАЮТ ✓" : "ЕСТЬ РАСХОЖДЕНИЯ ✗"));

        // Проверяем идентичность после модификаций
        System.out.println("\nПроверка после модификаций:");
        try {
            // Одинаковые операции с обеими функциями
            arrayFunc.addPoint(new FunctionPoint(1.2, 1.44));
            listFunc.addPoint(new FunctionPoint(1.2, 1.44));

            arrayFunc.deletePoint(2);
            listFunc.deletePoint(2);

            arrayFunc.setPointY(1, 2.0);
            listFunc.setPointY(1, 2.0);

            System.out.println("Массив после модификаций:");
            arrayFunc.printFunction();
            System.out.println("Список после модификаций:");
            listFunc.printFunction();

        } catch (Exception e) {
            System.out.println("Ошибка при модификациях: " + e.getMessage());
        }
    }
}