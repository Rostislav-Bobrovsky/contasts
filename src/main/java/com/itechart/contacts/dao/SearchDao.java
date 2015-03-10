package com.itechart.contacts.dao;

import com.itechart.contacts.model.People;
import com.itechart.contacts.model.SearchDto;

import java.util.List;

/**
 * Created by Rostislav on 09-Mar-15.)
 */
public interface SearchDao {
    List<People> getBySearch(SearchDto searchDto);

    List<String> getSelectEmails(String[] ids);
}
