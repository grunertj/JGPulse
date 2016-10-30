public class Smooth {
  private float [][] buffer;
  private int count = 10;
  private int center = 5;

  public Smooth () {
    buffer = new float[10][count];
    for (int j = 0; j < 10; j++) {
      for (int i = 0 ; i < count ; i++) {
        buffer[j][i] = 0.0f;
      }
    }
    System.out.println("Count: "+count+" Center: "+center);
  }

  public Smooth (int c) {
    count = c;
    center = c / 2 + c % 2;
    buffer = new float[10][count];
    for (int j = 0; j < 10; j++) {
      for (int i = 0 ; i < count ; i++) {
        buffer[j][i] = 0.0f;
      }
    }
    System.out.println("Count: "+count+" Center: "+center);
  }

  public float avg (float data) {
    float total = 0.0f;
    int i = 0;

    for (i = count - 1 ; i > 0 ; i--) {
      buffer[0][i] = buffer[0][i-1];
    }
    buffer[0][0] = data;

    for (i = 0 ; i < count ; i++) {
      total = total + buffer[0][i];
    }

    return (total/count);
  }

  public float avg (String data_string) {
    float data = 0.0f;
    data = Float.parseFloat(data_string);
    float total = 0.0f;
    int i = 0;

    for (i = count - 1 ; i > 0 ; i--) {
      buffer[1][i] = buffer[1][i-1];
    }
    buffer[1][0] = data;

    for (i = 0 ; i < count ; i++) {
      total = total + buffer[1][i];
    }

    return (total/count);
  }

  public float max (float data) {
    int i = 0;
    int c = center - 1;

    for (i = count - 1 ; i > 0 ; i--) {
      buffer[2][i] = buffer[2][i-1];
    }
    buffer[2][0] = data;

    for (i = 0 ; i < c ; i++) {
      if (buffer[2][i] > buffer[2][c]) {
	return 0;
      }
    }

    for (i = c + 1 ; i < count ; i++) {
      if (buffer[2][i] > buffer[2][c]) {
	return 0;
      }
    }

    return buffer[2][c];
  }

  public float max (String data_string) {
    float data = 0.0f;
    data = Float.parseFloat(data_string);
    int i = 0;
    int c = center - 1;

    for (i = count - 1 ; i > 0 ; i--) {
      buffer[3][i] = buffer[3][i-1];
    }
    buffer[3][0] = data;

    for (i = 0 ; i < c ; i++) {
      if (buffer[3][i] > buffer[3][c]) {
	return 0;
      }
    }

    for (i = c + 1 ; i < count ; i++) {
      if (buffer[3][i] > buffer[3][c]) {
	return 0;
      }
    }

    return buffer[3][c];
  }

  public boolean is_max (String data_string) {
    float data = 0.0f;
    data = Float.parseFloat(data_string);
    int i = 0;
    int c = center - 1;

    for (i = count - 1 ; i > 0 ; i--) {
      buffer[4][i] = buffer[4][i-1];
    }
    buffer[4][0] = data;

    for (i = 0 ; i < c ; i++) {
      if (buffer[4][i] > buffer[4][c]) {
	return false;
      }
    }

    for (i = c + 1 ; i < count ; i++) {
      if (buffer[4][i] > buffer[4][c]) {
	return false;
      }
    }

    return true;
  }

  public boolean is_max (float data) {
    int i = 0;
    int c = center - 1;

    for (i = count - 1 ; i > 0 ; i--) {
      buffer[5][i] = buffer[5][i-1];
    }
    buffer[5][0] = data;

    for (i = 0 ; i < c ; i++) {
      if (buffer[5][i] > buffer[5][c]) {
	return false;
      }
    }

    for (i = c + 1 ; i < count ; i++) {
      if (buffer[5][i] > buffer[5][c]) {
	return false;
      }
    }

    return true;
  }

  public float min (String data_string) {
    float data = 0.0f;
    data = Float.parseFloat(data_string);
    int i = 0;
    int c = center - 1;

    for (i = count - 1 ; i > 0 ; i--) {
      buffer[6][i] = buffer[6][i-1];
    }
    buffer[6][0] = data;

    for (i = 0 ; i < c ; i++) {
      if (buffer[6][i] < buffer[6][c]) {
	return 0;
      }
    }

    for (i = c + 1 ; i < count ; i++) {
      if (buffer[6][i] < buffer[6][c]) {
	return 0;
      }
    }

    return buffer[6][c];
  }

  public float min (float data) {
    int i = 0;
    int c = center - 1;

    for (i = count - 1 ; i > 0 ; i--) {
      buffer[7][i] = buffer[7][i-1];
    }
    buffer[7][0] = data;

    for (i = 0 ; i < c ; i++) {
      if (buffer[7][i] < buffer[7][c]) {
	return 0;
      }
    }

    for (i = c + 1 ; i < count ; i++) {
      if (buffer[7][i] < buffer[7][c]) {
	return 0;
      }
    }

    return buffer[7][c];
  }

  public boolean is_min (String data_string) {
    float data = 0.0f;
    data = Float.parseFloat(data_string);
    int i = 0;
    int c = center - 1;

    for (i = count - 1 ; i > 0 ; i--) {
      buffer[8][i] = buffer[8][i-1];
    }
    buffer[8][0] = data;

    for (i = 0 ; i < c ; i++) {
      if (buffer[8][i] < buffer[8][c]) {
	return false;
      }
    }

    for (i = c + 1 ; i < count ; i++) {
      if (buffer[8][i] < buffer[8][c]) {
	return false;
      }
    }

    return true;
  }

  public boolean is_min (float data) {
    int i = 0;
    int c = center - 1;

    for (i = count - 1 ; i > 0 ; i--) {
      buffer[9][i] = buffer[9][i-1];
    }
    buffer[9][0] = data;

    for (i = 0 ; i < c ; i++) {
      if (buffer[9][i] < buffer[9][c]) {
	return false;
      }
    }

    for (i = c + 1 ; i < count ; i++) {
      if (buffer[9][i] < buffer[9][c]) {
	return false;
      }
    }

    return true;
  }
}

