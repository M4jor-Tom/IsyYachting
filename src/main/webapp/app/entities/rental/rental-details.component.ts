import { Component, Vue, Inject } from 'vue-property-decorator';

import { IRental } from '@/shared/model/rental.model';
import RentalService from './rental.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class RentalDetails extends Vue {
  @Inject('rentalService') private rentalService: () => RentalService;
  @Inject('alertService') private alertService: () => AlertService;

  public rental: IRental = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.rentalId) {
        vm.retrieveRental(to.params.rentalId);
      }
    });
  }

  public retrieveRental(rentalId) {
    this.rentalService()
      .find(rentalId)
      .then(res => {
        this.rental = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
