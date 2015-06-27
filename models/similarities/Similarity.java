package models.similarities;

import models.Item;

public interface Similarity {

    Double calculate(Item x, Item y);
}
