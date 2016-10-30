#include "Processing.h"

Processing::Processing() {
  t0 = 0.0;
}

float Processing::Period(float input) {
  float ouput;
  ouput = input - t0;
  t0 = input;
  return ouput;
}

