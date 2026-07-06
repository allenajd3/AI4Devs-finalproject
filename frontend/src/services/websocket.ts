import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

class WebSocketService {
  private client: Client | null = null;
  private connected = false;

  connect(onMessage: (message: any) => void) {
    try {
      this.client = new Client({
        webSocketFactory: () => new SockJS('/ws'),
        debug: (str) => {
          console.log('STOMP: ' + str);
        },
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
      });

      this.client.onConnect = () => {
        console.log('WebSocket connected');
        this.connected = true;
        
        // Subscribe to generation progress updates
        this.client?.subscribe('/topic/generation-progress', (message) => {
          const data = JSON.parse(message.body);
          onMessage(data);
        });
      };

      this.client.onStompError = (frame) => {
        console.error('STOMP error:', frame);
        this.connected = false;
      };

      this.client.onWebSocketError = (error) => {
        console.warn('WebSocket error (backend may not be ready):', error);
        this.connected = false;
      };

      this.client.activate();
    } catch (error) {
      console.warn('Failed to initialize WebSocket:', error);
    }
  }

  disconnect() {
    if (this.client) {
      this.client.deactivate();
      this.connected = false;
    }
  }

  isConnected() {
    return this.connected;
  }
}

export const websocketService = new WebSocketService();
