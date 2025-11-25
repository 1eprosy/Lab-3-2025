package functions;

public class ArrayTabulatedFunction implements TabulatedFunction {
    private FunctionPoint[] points;

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        }

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        }

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
        }
    }

    @Override
    public int getPointsCount() {
    }

    @Override
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        }
        return new FunctionPoint(points[index]);
    }

    @Override
        }

        }

        points[index] = new FunctionPoint(point);
    }

    @Override
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        }
        return points[index].getX();
    }

    @Override
        }

        }
        }

    }

    @Override
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        }
        return points[index].getY();
    }

    @Override
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        }
    }

    @Override
        }
        }

    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        int insertIndex = 0;
            insertIndex++;
        }

        }

        }

        points[insertIndex] = new FunctionPoint(point);
    }

    @Override
            }
        }

    }

    }

    }

    }

    }
}