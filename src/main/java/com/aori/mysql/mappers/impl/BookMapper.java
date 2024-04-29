package com.aori.mysql.mappers.impl;

import com.aori.mysql.domain.dto.BookDto;
import com.aori.mysql.domain.entities.BookEntity;
import com.aori.mysql.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BookMapper implements Mapper<BookEntity, BookDto> {

    private ModelMapper modelMapper;

    public BookMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public BookDto mapTo(BookEntity book) {
        return modelMapper.map(book,BookDto.class);
    }

    @Override
    public BookEntity mapFrom(BookDto book) {
        return modelMapper.map(book,BookEntity.class);
    }
}
