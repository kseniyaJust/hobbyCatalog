package org.example.hobbycatalog.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedHobbiesResponseDTO {
    private List<HobbyDTO> content;           // Содержимое страницы
    private int pageNumber;                   // Номер текущей страницы
    private int pageSize;                     // Размер страницы
    private long totalElements;               // Всего элементов
    private int totalPages;                   // Всего страниц
    private boolean first;                    // Первая страница
    private boolean last;                     // Последняя страница
    private boolean empty;                    // Пустая страница
    private int numberOfElements;             // Количество элементов на странице
}