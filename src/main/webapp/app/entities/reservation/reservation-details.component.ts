import { Component, Vue, Inject } from 'vue-property-decorator';

import { IReservation } from '@/shared/model/reservation.model';
import ReservationService from './reservation.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ReservationDetails extends Vue {
  @Inject('reservationService') private reservationService: () => ReservationService;
  @Inject('alertService') private alertService: () => AlertService;

  public reservation: IReservation = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.reservationId) {
        vm.retrieveReservation(to.params.reservationId);
      }
    });
  }

  public retrieveReservation(reservationId) {
    this.reservationService()
      .find(reservationId)
      .then(res => {
        this.reservation = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
