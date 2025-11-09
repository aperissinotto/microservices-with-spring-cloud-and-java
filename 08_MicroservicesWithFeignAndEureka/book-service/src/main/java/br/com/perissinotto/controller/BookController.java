package br.com.perissinotto.controller;

import br.com.perissinotto.dto.ExchangeDto;
import br.com.perissinotto.environment.InstanceInformationService;
import br.com.perissinotto.model.Book;
import br.com.perissinotto.proxy.ExchangeProxy;
import br.com.perissinotto.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@RestController
@RequestMapping("book-service")
public class BookController {
    @Autowired
    private InstanceInformationService informationService;

    @Autowired
    private BookRepository repository;

    @Autowired
    private ExchangeProxy proxy;

    @GetMapping(value = "/{id}/{currency}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Book findBook(
            @PathVariable("id") Long id,
            @PathVariable("currency") String currency
    ) {
        String port = informationService.retrieveServerPort();
        var book = repository.findById(id).orElseThrow();


        ExchangeDto exchangeDto = proxy.getExchange(book.getPrice(), "USD", currency);

        book.setEnvironment("Book PORT: " + port + " Exchange PORT: " + exchangeDto.getEnvironment());
        book.setPrice(exchangeDto.getConvertedValue());
        book.setCurrency(currency);
        return book;
    }
}
