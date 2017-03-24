export class Server {
    private url: string = "http://localhost:3000/api";
    
	getUrl(): string  {
		return this.url;
	}
}