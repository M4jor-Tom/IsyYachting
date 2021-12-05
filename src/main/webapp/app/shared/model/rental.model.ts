import { IReservation } from '@/shared/model/reservation.model';
import { IRentalPrice } from '@/shared/model/rental-price.model';

export interface IRental {
  id?: number;
  beginingInstant?: Date;
  endingInstant?: Date;
  reservationLists?: IReservation[] | null;
  rentalPriceLists?: IRentalPrice[] | null;
}

export class Rental implements IRental {
  constructor(
    public id?: number,
    public beginingInstant?: Date,
    public endingInstant?: Date,
    public reservationLists?: IReservation[] | null,
    public rentalPriceLists?: IRentalPrice[] | null
  ) {}
}
