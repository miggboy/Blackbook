package blackbook.backend;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;

public class CustomMapLayer extends MapLayer{
	private Node marker;
	private MapPoint p1;
	
	public CustomMapLayer(MapPoint mapPoint) {
		p1 = mapPoint;
		String str = getClass().getResource("/IMG/location.png").toString();
		Image img = new Image(str);
		marker = new ImageView(img);
		
        ((ImageView) marker).setFitWidth(23);  // Set the width of the marker
        ((ImageView) marker).setFitHeight(35);
		
		getChildren().add(marker);
	}
	
	@Override
	public void layoutLayer() {
		Point2D point = getMapPoint(p1.getLatitude(), p1.getLongitude());
		
	    double offsetX = -((ImageView) marker).getFitWidth() / 2.0;
	    double offsetY = -((ImageView) marker).getFitHeight();
		
		marker.setTranslateX(point.getX() + offsetX);
		marker.setTranslateY(point.getY() + offsetY);
	}	
}
