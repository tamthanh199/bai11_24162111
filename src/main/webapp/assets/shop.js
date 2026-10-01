"use strict";

document.addEventListener("click", function (event) {
    const button = event.target.closest("[data-step]");
    if (!button) {
        return;
    }
    const input = button.closest(".quantity-control").querySelector("input");
    const minimum = Number(input.min);
    const maximum = Number(input.max);
    const value = Number(input.value);
    input.value = Math.max(minimum, Math.min(maximum, (Number.isFinite(value) ? value : minimum)
        + Number(button.dataset.step)));
    input.dispatchEvent(new Event("change", { bubbles: true }));
});

document.addEventListener("submit", function (event) {
    const form = event.target;
    if (form.dataset.confirm && !window.confirm(form.dataset.confirm)) {
        event.preventDefault();
        return;
    }
    if (form.hasAttribute("data-checkout")) {
        const button = form.querySelector('button[type="submit"]');
        button.disabled = true;
        button.textContent = "Đang tạo đơn hàng…";
    }
});

window.addEventListener("pageshow", function () {
    const button = document.querySelector('[data-checkout] button[type="submit"]');
    if (button && button.textContent === "Đang tạo đơn hàng…") {
        button.disabled = false;
        button.textContent = "Xác nhận đặt hàng";
    }
});
