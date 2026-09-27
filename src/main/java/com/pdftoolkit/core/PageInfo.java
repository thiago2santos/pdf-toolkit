package com.pdftoolkit.core;

/**
 * Página de um PDF, com tamanho já considerando a rotação.
 *
 * @param number número da página, começando em 1
 * @param width largura em pontos (1/72 pol.)
 * @param height altura em pontos (1/72 pol.)
 */
public record PageInfo(int number, float width, float height) {}
