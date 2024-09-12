package com.isep.acme.applicationServices.interfaces.http;

import java.time.LocalDate;

public interface IFunFactRepository {

    String fetchFunFact(LocalDate date);
}
