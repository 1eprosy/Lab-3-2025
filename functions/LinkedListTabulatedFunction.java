package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction {

    private static class FunctionNode {
        private FunctionPoint point;
        private FunctionNode prev;
        private FunctionNode next;

        public FunctionNode(FunctionPoint point) {
            this.point = point;
        }

        public FunctionNode(FunctionPoint point, FunctionNode prev, FunctionNode next) {
            this.point = point;
            this.prev = prev;
            this.next = next;
        }
    }

    private FunctionNode head;
    private int pointsCount;
    private FunctionNode lastAccessedNode;
    private int lastAccessedIndex;

    // КОНСТРУКТОРЫ
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX || pointsCount < 2) {
            throw new IllegalArgumentException("Некорректные параметры функции");
        }

        initializeList();
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            addNodeToTail().point = new FunctionPoint(x, 0);
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX || values.length < 2) {
            throw new IllegalArgumentException("Некорректные параметры функции");
        }

        initializeList();
        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            double x = leftX + i * step;
            addNodeToTail().point = new FunctionPoint(x, values[i]);
        }
    }

    private void initializeList() {
        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        pointsCount = 0;
        lastAccessedNode = head;
        lastAccessedIndex = -1;
    }

    // ВНУТРЕННИЕ МЕТОДЫ ДЛЯ РАБОТЫ СО СПИСКОМ (без изменений)
    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }

        if (lastAccessedIndex != -1) {
            int distanceFromLast = Math.abs(index - lastAccessedIndex);
            int distanceFromStart = index;
            int distanceFromEnd = pointsCount - 1 - index;

            if (distanceFromLast <= distanceFromStart && distanceFromLast <= distanceFromEnd) {
                return moveFromLastAccessed(index);
            } else if (distanceFromStart <= distanceFromEnd) {
                return moveFromStart(index);
            } else {
                return moveFromEnd(index);
            }
        } else {
            return moveFromStart(index);
        }
    }

    private FunctionNode moveFromLastAccessed(int targetIndex) {
        FunctionNode current = lastAccessedNode;
        int currentIndex = lastAccessedIndex;

        while (currentIndex < targetIndex) {
            current = current.next;
            currentIndex++;
        }
        while (currentIndex > targetIndex) {
            current = current.prev;
            currentIndex--;
        }

        lastAccessedNode = current;
        lastAccessedIndex = currentIndex;
        return current;
    }

    private FunctionNode moveFromStart(int index) {
        FunctionNode current = head.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        lastAccessedNode = current;
        lastAccessedIndex = index;
        return current;
    }

    private FunctionNode moveFromEnd(int index) {
        FunctionNode current = head.prev;
        for (int i = pointsCount - 1; i > index; i--) {
            current = current.prev;
        }
        lastAccessedNode = current;
        lastAccessedIndex = index;
        return current;
    }

    private FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode(null, head.prev, head);
        head.prev.next = newNode;
        head.prev = newNode;
        pointsCount++;
        lastAccessedNode = newNode;
        lastAccessedIndex = pointsCount - 1;
        return newNode;
    }

    private FunctionNode addNodeByIndex(int index) {
        if (index < 0 || index > pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }

        if (index == pointsCount) {
            return addNodeToTail();
        }

        FunctionNode nextNode = getNodeByIndex(index);
        FunctionNode prevNode = nextNode.prev;
        FunctionNode newNode = new FunctionNode(null, prevNode, nextNode);

        prevNode.next = newNode;
        nextNode.prev = newNode;
        pointsCount++;
        lastAccessedNode = newNode;
        lastAccessedIndex = index;
        return newNode;
    }

    private FunctionNode deleteNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        if (pointsCount <= 2) {
            throw new IllegalStateException("Нельзя удалить точку - минимальное количество точек: 2");
        }

        FunctionNode nodeToDelete = getNodeByIndex(index);
        FunctionNode prevNode = nodeToDelete.prev;
        FunctionNode nextNode = nodeToDelete.next;

        prevNode.next = nextNode;
        nextNode.prev = prevNode;
        pointsCount--;

        if (lastAccessedIndex == index) {
            lastAccessedNode = head;
            lastAccessedIndex = -1;
        } else if (lastAccessedIndex > index) {
            lastAccessedIndex--;
        }

        return nodeToDelete;
    }

    // РЕАЛИЗАЦИЯ МЕТОДОВ ИНТЕРФЕЙСА
    @Override
    public double getLeftDomainBorder() {
        if (pointsCount == 0) return Double.NaN;
        return head.next.point.getX();
    }

    @Override
    public double getRightDomainBorder() {
        if (pointsCount == 0) return Double.NaN;
        return head.prev.point.getX();
    }

    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() - 1e-10 || x > getRightDomainBorder() + 1e-10) {
            return Double.NaN;
        }

        FunctionNode startNode = (lastAccessedNode != head) ? lastAccessedNode : head.next;
        FunctionNode current = startNode;

        while (current != head && current.next != head) {
            double currentX = current.point.getX();
            double nextX = current.next.point.getX();

            // Проверяем точное совпадение с текущей точкой (с машинным эпсилоном)
            if (Math.abs(x - currentX) < 1e-10) {
                lastAccessedNode = current;
                lastAccessedIndex = getNodeIndex(current);
                return current.point.getY();
            }

            // Проверяем точное совпадение со следующей точкой (с машинным эпсилоном)
            if (Math.abs(x - nextX) < 1e-10) {
                lastAccessedNode = current.next;
                lastAccessedIndex = getNodeIndex(current.next);
                return current.next.point.getY();
            }

            // Проверяем нахождение в интервале (с машинным эпсилоном)
            if (x >= currentX - 1e-10 && x <= nextX + 1e-10) {
                lastAccessedNode = current;
                lastAccessedIndex = getNodeIndex(current);

                double x1 = currentX;
                double y1 = current.point.getY();
                double x2 = nextX;
                double y2 = current.next.point.getY();

                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }

            // Определяем направление движения (с машинным эпсилоном)
            if (x < currentX - 1e-10) {
                current = current.prev;
            } else {
                current = current.next;
            }
        }

        return Double.NaN;
    }

    @Override
    public int getPointsCount() {
        return pointsCount;
    }

    @Override
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        return new FunctionPoint(getNodeByIndex(index).point);
    }

    @Override
    public void setPoint(int index, FunctionPoint point)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);

        if (index > 0 && point.getX() <= getNodeByIndex(index - 1).point.getX()) {
            throw new InappropriateFunctionPointException(point);
        }
        if (index < pointsCount - 1 && point.getX() >= getNodeByIndex(index + 1).point.getX()) {
            throw new InappropriateFunctionPointException(point);
        }

        node.point = new FunctionPoint(point);
    }

    @Override
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        return getNodeByIndex(index).point.getX();
    }

    @Override
    public void setPointX(int index, double x)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);

        if (index > 0 && x <= getNodeByIndex(index - 1).point.getX()) {
            throw new InappropriateFunctionPointException("New X coordinate " + x +
                    " is inappropriate for point at index " + index);
        }
        if (index < pointsCount - 1 && x >= getNodeByIndex(index + 1).point.getX()) {
            throw new InappropriateFunctionPointException("New X coordinate " + x +
                    " is inappropriate for point at index " + index);
        }

        node.point.setX(x);
    }

    @Override
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        return getNodeByIndex(index).point.getY();
    }

    @Override
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        getNodeByIndex(index).point.setY(y);
    }

    @Override
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException {
        deleteNodeByIndex(index);
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode startNode = (lastAccessedNode != head) ? lastAccessedNode : head.next;
        FunctionNode current = startNode;
        int insertIndex = 0;

        if (point.getX() >= startNode.point.getX()) {
            insertIndex = getNodeIndex(startNode);
            current = startNode;
            while (current != head && current.point.getX() < point.getX()) {
                current = current.next;
                insertIndex++;
            }
        } else {
            insertIndex = getNodeIndex(startNode);
            current = startNode;
            while (current != head && current.point.getX() > point.getX()) {
                current = current.prev;
                insertIndex--;
            }
            insertIndex++;
        }

        if (current != head && Math.abs(current.point.getX() - point.getX()) < 1e-10) {
            throw new InappropriateFunctionPointException(point);
        }

        FunctionNode newNode = addNodeByIndex(insertIndex);
        newNode.point = new FunctionPoint(point);
    }

    @Override
    public void printFunction() {
        System.out.println("Табулированная функция (связный список):");
        FunctionNode current = head.next;
        while (current != head) {
            System.out.printf("(%.6f; %.6f) ", current.point.getX(), current.point.getY());
            current = current.next;
        }
        System.out.println();
    }

    // Вспомогательные методы
    private int getNodeIndex(FunctionNode node) {
        FunctionNode current = head.next;
        int index = 0;
        while (current != head) {
            if (current == node) return index;
            current = current.next;
            index++;
        }
        return -1;
    }

    public void printListStructure() {
        System.out.println("Структура списка:");
        FunctionNode current = head;
        do {
            System.out.printf("[%s] <-> ",
                    (current.point == null) ? "HEAD" :
                            String.format("(%.1f;%.1f)", current.point.getX(), current.point.getY()));
            current = current.next;
        } while (current != head);
        System.out.println("[HEAD]");
    }
}