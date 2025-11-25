package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction {
    // Внутренний класс для элемента списка (остается без изменений)
    protected class FunctionNode {
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

        public FunctionPoint getPoint() {
            return point;
        }

        public void setPoint(FunctionPoint point) {
            this.point = point;
        }

        public FunctionNode getPrev() {
            return prev;
        }

        public void setPrev(FunctionNode prev) {
            this.prev = prev;
        }

        public FunctionNode getNext() {
            return next;
        }

        public void setNext(FunctionNode next) {
            this.next = next;
        }
    }

    // Поля основного класса
    private FunctionNode head;
    private int pointsCount;
    private FunctionNode lastAccessedNode;
    private int lastAccessedIndex;

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (pointsCount < 2) {
            throw new IllegalArgumentException("pointsCount must be at least 2");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("leftX must be less than rightX");
        }

        initializeList();
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            addNodeToTail().setPoint(new FunctionPoint(x, 0)); // Y = 0 по умолчанию
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (values.length < 2) {
            throw new IllegalArgumentException("values array must have at least 2 elements");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("leftX must be less than rightX");
        }

        initializeList();
        int pointsCount = values.length;
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            addNodeToTail().setPoint(new FunctionPoint(x, values[i])); // Используем переданные значения Y
        }
    }

    // Инициализация пустого списка с головой
    private void initializeList() {
        head = new FunctionNode(null);
        head.setPrev(head);
        head.setNext(head);
        pointsCount = 0;
        lastAccessedNode = head;
        lastAccessedIndex = -1;
    }

    // Методы работы со списком (остаются без изменений)
    protected FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }

        FunctionNode current;
        int startIndex;

        if (lastAccessedNode != head && lastAccessedIndex != -1) {
            int distanceFromLast = Math.abs(index - lastAccessedIndex);
            int distanceFromStart = index;
            int distanceFromEnd = pointsCount - 1 - index;

            if (distanceFromLast <= distanceFromStart && distanceFromLast <= distanceFromEnd) {
                current = lastAccessedNode;
                startIndex = lastAccessedIndex;
            } else if (distanceFromStart <= distanceFromEnd) {
                current = head.getNext();
                startIndex = 0;
            } else {
                current = head.getPrev();
                startIndex = pointsCount - 1;
            }
        } else {
            current = head.getNext();
            startIndex = 0;
        }

        if (index > startIndex) {
            for (int i = startIndex; i < index; i++) {
                current = current.getNext();
            }
        } else if (index < startIndex) {
            for (int i = startIndex; i > index; i--) {
                current = current.getPrev();
            }
        }

        lastAccessedNode = current;
        lastAccessedIndex = index;

        return current;
    }

    protected FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode(null, head.getPrev(), head);
        head.getPrev().setNext(newNode);
        head.setPrev(newNode);
        pointsCount++;

        lastAccessedNode = newNode;
        lastAccessedIndex = pointsCount - 1;

        return newNode;
    }

    protected FunctionNode addNodeByIndex(int index) {
        if (index < 0 || index > pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }

        if (index == pointsCount) {
            return addNodeToTail();
        }

        FunctionNode nodeAtIndex = getNodeByIndex(index);
        FunctionNode newNode = new FunctionNode(null, nodeAtIndex.getPrev(), nodeAtIndex);

        nodeAtIndex.getPrev().setNext(newNode);
        nodeAtIndex.setPrev(newNode);
        pointsCount++;

        lastAccessedNode = newNode;
        lastAccessedIndex = index;

        return newNode;
    }

    protected FunctionNode deleteNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        if (pointsCount <= 2) {
            throw new IllegalStateException("Cannot delete point - function must have at least 2 points");
        }

        FunctionNode nodeToDelete = getNodeByIndex(index);

        nodeToDelete.getPrev().setNext(nodeToDelete.getNext());
        nodeToDelete.getNext().setPrev(nodeToDelete.getPrev());
        pointsCount--;

        if (lastAccessedNode == nodeToDelete) {
            if (index < pointsCount) {
                lastAccessedNode = getNodeByIndex(index);
            } else if (pointsCount > 0) {
                lastAccessedNode = getNodeByIndex(pointsCount - 1);
                lastAccessedIndex = pointsCount - 1;
            } else {
                lastAccessedNode = head;
                lastAccessedIndex = -1;
            }
        } else if (lastAccessedIndex > index) {
            lastAccessedIndex--;
        }

        return nodeToDelete;
    }

    // МЕТОДЫ ТАБУЛИРОВАННОЙ ФУНКЦИИ (аналогичные TabulatedFunction)

    public int getPointsCount() {
        return pointsCount;
    }

    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        return new FunctionPoint(getNodeByIndex(index).getPoint());
    }

    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }

        FunctionNode node = getNodeByIndex(index);
        double newX = point.getX();
        double currentX = node.getPoint().getX();

        // Проверка изменения X координаты
        if (Math.abs(newX - currentX) > 1e-10) {
            // Проверка упорядоченности
            if (index > 0 && newX <= getNodeByIndex(index - 1).getPoint().getX()) {
                throw new InappropriateFunctionPointException("New x must be greater than previous point's x");
            }
            if (index < pointsCount - 1 && newX >= getNodeByIndex(index + 1).getPoint().getX()) {
                throw new InappropriateFunctionPointException("New x must be less than next point's x");
            }
        }

        node.setPoint(new FunctionPoint(point));
    }

    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        return getNodeByIndex(index).getPoint().getX();
    }

    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }

        FunctionNode node = getNodeByIndex(index);
        double currentX = node.getPoint().getX();
        double y = node.getPoint().getY();

        if (Math.abs(x - currentX) < 1e-10) {
            return;
        }

        // Проверка упорядоченности
        if (index > 0 && x <= getNodeByIndex(index - 1).getPoint().getX()) {
            throw new InappropriateFunctionPointException("New x must be greater than previous point's x");
        }
        if (index < pointsCount - 1 && x >= getNodeByIndex(index + 1).getPoint().getX()) {
            throw new InappropriateFunctionPointException("New x must be less than next point's x");
        }

        node.setPoint(new FunctionPoint(x, y));
    }

    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        return getNodeByIndex(index).getPoint().getY();
    }

    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }

        FunctionNode node = getNodeByIndex(index);
        double x = node.getPoint().getX();
        node.setPoint(new FunctionPoint(x, y));
    }

    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        // Находим позицию для вставки
        FunctionNode current = head.getNext();
        int insertIndex = 0;

        while (current != head && current.getPoint().getX() < point.getX()) {
            current = current.getNext();
            insertIndex++;
        }

        // Проверка на существование точки с таким же X
        if (current != head && Math.abs(current.getPoint().getX() - point.getX()) < 1e-10) {
            throw new InappropriateFunctionPointException("Point with x=" + point.getX() + " already exists");
        }

        // Вставляем новую точку
        FunctionNode newNode = addNodeByIndex(insertIndex);
        newNode.setPoint(new FunctionPoint(point));
    }

    // ОПТИМИЗИРОВАННЫЕ МЕТОДЫ

    public double getLeftDomainBorder() {
        if (pointsCount == 0) {
            return Double.NaN;
        }
        // Оптимизация: прямой доступ к первому элементу
        return head.getNext().getPoint().getX();
    }

    public double getRightDomainBorder() {
        if (pointsCount == 0) {
            return Double.NaN;
        }
        // Оптимизация: прямой доступ к последнему элементу
        return head.getPrev().getPoint().getX();
    }

    /**
     * Оптимизированный метод вычисления значения функции
     * Использует lastAccessedNode для ускорения последовательных вызовов
     */
    @Override
    public double getFunctionValue(double x) {
        if (pointsCount == 0) {
            return Double.NaN;
        }

        double leftBorder = getLeftDomainBorder();
        double rightBorder = getRightDomainBorder();

        // Проверка границ с учетом машинного эпсилона
        if (x < leftBorder - 1e-10 || x > rightBorder + 1e-10) {
            return Double.NaN;
        }

        // Начинаем с первого элемента
        FunctionNode current = head.getNext();

        while (current != head && current.getNext() != head) {
            double currentX = current.getPoint().getX();
            double nextX = current.getNext().getPoint().getX();

            // Проверка точного совпадения с текущей точкой
            if (Math.abs(x - currentX) < 1e-10) {
                lastAccessedNode = current;
                lastAccessedIndex = getNodeIndex(current);
                return current.getPoint().getY();
            }

            // Проверка точного совпадения со следующей точкой
            if (Math.abs(x - nextX) < 1e-10) {
                lastAccessedNode = current.getNext();
                lastAccessedIndex = getNodeIndex(current.getNext());
                return current.getNext().getPoint().getY();
            }

            // Проверка нахождения в интервале (включая границы)
            if (x >= currentX - 1e-10 && x <= nextX + 1e-10) {
                lastAccessedNode = current;
                lastAccessedIndex = getNodeIndex(current);
                return linearInterpolation(current.getPoint(), current.getNext().getPoint(), x);
            }

            current = current.getNext();
        }

        return Double.NaN;
    }
    // Вспомогательные методы
    private int getNodeIndex(FunctionNode node) {
        FunctionNode current = head.getNext();
        int index = 0;
        while (current != head) {
            if (current == node) {
                return index;
            }
            current = current.getNext();
            index++;
        }
        return -1;
    }

    private double linearInterpolation(FunctionPoint p1, FunctionPoint p2, double x) {
        double x1 = p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX();
        double y2 = p2.getY();

        return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
    }
}