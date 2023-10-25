package dominio;

public class DistanciaCoordenadas {

	private double x;
	private double y;
	
	public DistanciaCoordenadas(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		
		return this.x;
		
	}
	
	public void setX(double x) {
		
		this.x = x;
		
	}
	
	public double getY() {
	
		return this.y;
	
	}
	
	public void setY(double y) {
		
		this.y = y;
		
	}
	
	public String toString() {
		return "DistanciaCoordenadas [distanciaX= " + x +", distanciaY= " + y + "]";
	}
	
	public static DistanciaCoordenadas obtenerDistancia(double x1, double y1, double x2, double y2) {
		return new DistanciaCoordenadas((x1-x2), (y1-y2));		
	}
	
}
