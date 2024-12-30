import React, { useEffect, useState } from "react";
import { Client, IMessage } from "@stomp/stompjs";
import WebSocketService from '../API/socketService';
import { fetchNotifications } from "../API/NotificationAPI";


interface Notification {
  date: string;
  message: string;
  link:string;
}

interface NotificationConsumerProps {
  userId: number;
}

const NotificationConsumer: React.FC<NotificationConsumerProps> = ({ userId }) => {
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const webSocketService = new WebSocketService("ws://localhost:8080/ws");

  useEffect(() => {
    const initNotifications = async()=>{
      try{
        const notifications: Notification[] = await fetchNotifications();
        setNotifications(notifications);
        const id = parseInt(localStorage.getItem("id")!);
        
        webSocketService.connect(onConnect, onError, id);
      }catch(e){
        console.error(e)
      }
    }

    const onConnect = () => {
      console.log('Connected to WebSocket');
      const id = parseInt(localStorage.getItem("id")!);
      webSocketService.subscribe(`/topic/notifications/${id}`, (message) => {
        console.log(JSON.parse(message.body));
        setNotifications((prev) => [...prev, JSON.parse(message.body)])
      });
      
    }
    const onError = (error: string) => {
      console.error('WebSocket error:', error);
    };
    initNotifications();
    return () => {
      webSocketService.disconnect();
    };
  }, [userId]);

  return (
    <div>
      <h2>Notifications</h2>
      <ul>
        {notifications.map((notif, index) => (
          <li key={index}>{notif.message}</li>
        ))}
      </ul>
    </div>
  );
};

export default NotificationConsumer;
