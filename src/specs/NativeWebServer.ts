import {TurboModuleRegistry} from "react-native";
import type {TurboModule} from "react-native";
import type { Int32 } from "react-native/Libraries/Types/CodegenTypes";

export type HandlerOptions = {
  /**
   * URL path prefix for requests. Use '/' for root directory.
   * For example: if prefix is '/game', requests to '/game/index.html' will be served.
   */
  prefix: string;
  /**
   * Local file system path to the directory that should be served.
   * For example: if prefix is '/game' and path is '/Documents/game',
   * then accessing '/game/index.html' will serve the file at '/Documents/game/index.html'.
   */
  path: string;
};

export interface Spec extends TurboModule {
  /**
   * Start the web server with the specified path handlers.
   * Can be called multiple times. If the server is already running,
   * it will stop the previous instance and start a new one with the new configuration.
   * 
   * @param handlers Array of path handler configurations that map URL prefixes to local directories
   * @returns Promise that resolves to the port number where the server is listening
   */
  start(handlers: HandlerOptions[]): Promise<Int32>;

  /**
   * Stop the web server and release all resources.
   * Safe to call even if the server is not running.
   */
  stop(): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>(
  'WebServer',
) as Spec;
