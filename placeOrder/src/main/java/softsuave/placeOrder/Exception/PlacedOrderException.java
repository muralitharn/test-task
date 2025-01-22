package softsuave.placeOrder.Exception;

public class PlacedOrderException extends Exception
{
    public PlacedOrderException(String message){
        super(message);
    }

    public PlacedOrderException(String message,Throwable error){
        super(message,error);
    }
}

