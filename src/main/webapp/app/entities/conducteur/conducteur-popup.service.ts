import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Conducteur } from './conducteur.model';
import { ConducteurService } from './conducteur.service';

@Injectable()
export class ConducteurPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private conducteurService: ConducteurService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.conducteurService.find(id).subscribe((conducteur) => {
                    conducteur.livreeLe = this.datePipe
                        .transform(conducteur.livreeLe, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.conducteurModalRef(component, conducteur);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.conducteurModalRef(component, new Conducteur());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    conducteurModalRef(component: Component, conducteur: Conducteur): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.conducteur = conducteur;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
