package dev.inteleonyx.armandillo.utils.exceptions;

public class RegistryException extends RuntimeException {
    public RegistryException(String message) {
        super(message);
    }

  public static class Duplicate extends RegistryException {
    public Duplicate(String id, String registryName) {
      super("Duplicate entry in [" + registryName + "]: '" + id + "' is already registered.");
    }
  }

  public static class Frozen extends RegistryException {
    public Frozen(String registryName) {
      super("Registry [" + registryName + "] is frozen and cannot accept new entries.");
    }
  }

  public static class NotFound extends RegistryException {
    public NotFound(String id, String registryName) {
      super("Entry '" + id + "' not found in registry [" + registryName + "].");
    }
  }
}
