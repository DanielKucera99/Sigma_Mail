package cz.uhk.sigmamail.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryDAOImpl implements CategoryDAO{

    private EntityManager entityManager;

    @Autowired
    public CategoryDAOImpl(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    @Override
    public Category getCategoryById(int id){
        return entityManager.find(Category.class, id);
    }

    @Override
    public List<Category> getAllCategories() {
        TypedQuery<Category> query = entityManager.createQuery("FROM Category", Category.class);
        return query.getResultList();
    }
}
