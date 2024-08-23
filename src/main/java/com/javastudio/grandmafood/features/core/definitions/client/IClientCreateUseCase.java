package com.javastudio.grandmafood.features.core.definitions.client;

import com.javastudio.grandmafood.features.core.entities.client.Client;
import com.javastudio.grandmafood.features.core.entities.client.ClientCreateInput;

public interface IClientCreateUseCase {

    Client create(ClientCreateInput input);
}
