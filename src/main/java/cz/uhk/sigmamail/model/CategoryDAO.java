package cz.uhk.sigmamail.model;

import java.util.List;

public interface CategoryDAO {
    Category getCategoryById(int id);
    List<Category> getAllCategories();
}
