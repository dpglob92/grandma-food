package com.javastudio.grandmafood.core.unit.client;

import com.javastudio.grandmafood.features.core.database.adapters.ClientAdapter;
import com.javastudio.grandmafood.features.core.database.entities.ClientJPAEntity;
import com.javastudio.grandmafood.features.core.database.repositories.ClientJPAEntityRepository;
import com.javastudio.grandmafood.features.core.entities.client.Client;
import com.javastudio.grandmafood.features.core.entities.client.DocumentType;
import com.javastudio.grandmafood.features.core.usecases.client.ClientFindUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ClientFindUseCaseTest {

    @Mock
    private ClientJPAEntityRepository repository;

    @Mock
    private ClientAdapter clientAdapter;

    @InjectMocks
    private ClientFindUseCase clientFindUseCase;

    @Test
    public void Should_ReturnClientsSortedByNameAsc_WhenOrderByName() {

        List<ClientJPAEntity> listClients = List.of(
                ClientJPAEntity.builder()
                        .name("John")
                        .documentType(DocumentType.CC)
                        .documentId("123456")
                        .email("john@example.com")
                        .phone("555-122334")
                        .deliveryAddress("123 Main St")
                        .build(),
                ClientJPAEntity.builder()
                        .name("Alice")
                        .documentType(DocumentType.CC)
                        .documentId("789456")
                        .email("alice@example.com")
                        .phone("555-567238")
                        .deliveryAddress("456 Elm St")
                        .build()
        );
        when(repository.findAll(Sort.by(Sort.Direction.ASC, "name"))).thenReturn(listClients);

        List<Client> clients = clientFindUseCase.getClientsSorted("NAME", "ASC");

        Assertions.assertThat(clients).hasSize(2);
        verify(repository, times(1)).findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Test
    public void Should_ReturnClientsSortedByDocumentDesc_WhenOrderByDocument() {
        List<ClientJPAEntity> listClients = List.of(
                ClientJPAEntity.builder()
                        .name("John")
                        .documentType(DocumentType.CC)
                        .documentId("123456")
                        .email("john@example.com")
                        .phone("555-122334")
                        .deliveryAddress("123 Main St")
                        .build(),
                ClientJPAEntity.builder()
                        .name("Alice")
                        .documentType(DocumentType.CC)
                        .documentId("789456")
                        .email("alice@example.com")
                        .phone("555-567238")
                        .deliveryAddress("456 Elm St")
                        .build()
        );
        when(repository.findAll(Sort.by(Sort.Direction.DESC, "documentId"))).thenReturn(listClients);

        List<Client> clients = clientFindUseCase.getClientsSorted("DOCUMENT", "DESC");

        Assertions.assertThat(clients).hasSize(2);
        verify(repository, times(1)).findAll(Sort.by(Sort.Direction.DESC, "documentId"));
    }
}
