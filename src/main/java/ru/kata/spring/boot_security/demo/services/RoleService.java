package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.Optional;

public interface RoleService {

    public Optional<Role> getRoleByName(String name);
}