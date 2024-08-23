package com.javastudio.grandmafood.features.core.definitions.client;

import com.javastudio.grandmafood.features.core.entities.client.Client;
import com.javastudio.grandmafood.features.core.entities.client.ClientUpdateInput;

public interface IClientUpdateUseCase {

    void update(Client client, ClientUpdateInput input);

    void updateByDocument(String documentId, ClientUpdateInput input);
}
