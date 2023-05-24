import { useState, useRef, useEffect } from "react";
import "./App.css";

function App() {
  const [input, setInput] = useState("");
  const [messages, setMessages] = useState([]);
  const isFirstRender = useRef(true);

  const handleMessageSubmit = async () => {
    setMessages((prevMessages) => [
      ...prevMessages,
      { message: input, author: 'user' },
    ]);
    setInput("");
  };

  useEffect( () => {
    async function fetchData() {
      const response = await sendMessageToBackend(messages);
      setMessages((prevMessages) => [
        ...prevMessages,
        { message: response, author: 'assistant' },
      ]);
    }

    if (isFirstRender.current) {
      isFirstRender.current = false;
      return; 
    }

    if(messages.length >0 && messages[messages.length - 1].author === 'user'){
      fetchData()
    }
 
  }, [messages]);

  async function sendMessageToBackend(messages) {
    const response = await fetch('http://localhost:8080/ai', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ messages: messages }),
    });
    const data = await response.json();
    return data.message.content
  }

  return (
    <div className="App">
      <div className="chat">
        {messages.map((message, index) => (
          <div
            key={index}
            className={message.author === 'user' ? "user-message" : "bot-message"}
          >
            {message.message}
          </div>
        ))}
      </div>
      <div className="input-container">
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
        />
        <button onClick={handleMessageSubmit}>Send</button>
      </div>
    </div>
  );
}

export default App;