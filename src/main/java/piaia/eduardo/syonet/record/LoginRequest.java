package piaia.eduardo.syonet.record;

import javax.validation.constraints.NotEmpty;

public record LoginRequest(@NotEmpty String username, @NotEmpty String password) {}
