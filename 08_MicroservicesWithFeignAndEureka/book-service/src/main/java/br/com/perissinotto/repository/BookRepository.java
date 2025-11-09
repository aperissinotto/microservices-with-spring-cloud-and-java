package br.com.perissinotto.repository;

import br.com.perissinotto.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {}
