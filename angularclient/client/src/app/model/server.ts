export class Server {
    private url: string = "http://localhost:8080/api";
    
	getUrl(): string  {
		return this.url;
	}
}