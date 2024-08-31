package com.onur.quote_proj.service;

import com.onur.quote_proj.model.Quote;
import com.onur.quote_proj.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class QuoteService {

    @Autowired
    private QuoteRepository repository;

    public Page<Quote> getQuotes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

//    public List<Quote> getAllQuotes() { return repository.findAll();
//    }

    public Quote getQuote(int id) {
        return repository.findById(id).orElse(null);
    }

    public Quote getRandomQuote() {
        List<Quote> quotes = repository.findAll();
        if (quotes.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return quotes.get(random.nextInt(quotes.size()));
    }

    public List<Quote> searchQuotes(String author, String text) {
        List<Quote> quotes = repository.findAll();
        return quotes.stream()
                .filter(quote -> (author == null || StringUtils.containsIgnoreCase(quote.getAuthor(), author)) &&
                        (text == null || StringUtils.containsIgnoreCase(quote.getText(), text)))
                .collect(Collectors.toList());
    }

    public Quote addQuote(Quote quote) {
        quote.setAuthor(quote.getAuthor());
        quote.setText(quote.getText());

        return repository.save(quote);
    }

    public Quote updateQuote(int id, Quote updatedQuote) {
        Quote existingQuote = repository.findById(id).orElse(null);

        if (existingQuote != null) {
            existingQuote.setAuthor(updatedQuote.getAuthor());
            existingQuote.setText(updatedQuote.getText());
            return repository.save(existingQuote);
        } else {
            return null;
        }
    }

    public void deleteQuote(int id) {
        repository.deleteById(id);
    }
}
