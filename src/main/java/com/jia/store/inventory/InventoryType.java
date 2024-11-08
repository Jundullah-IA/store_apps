package com.jia.store.inventory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InventoryType {
    T("Top Up"), W ("Withdrawal");

    private final String label;
}